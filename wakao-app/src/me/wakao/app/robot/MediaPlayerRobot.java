package me.wakao.app.robot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DecimalFormat;

import me.wakao.app.bean.RingObj;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class MediaPlayerRobot implements OnCompletionListener {
	public final static int MEDIA_LOADING = 1;
	public final static int MEDIA_START = 2;
	public final static int MEDIA_END = 3;
	public final static int MEDIA_LOADING_PROCESS_UPDATE = 4;
	public final static int MEDIA_RESET = 5;
	
	private final static int MAX_FAILCOUNT = 3; //最大下载失败次数，超过即不再重新下载
	
	private Context mContext;
	private MediaPlayer player;
	
	private RingObj ring;
	
	private int pausePos;
	
	private boolean isPause = false;
	
	private String mediaId;
	private String mediaUrl;
	
	private Handler handler;
	
	private File cache_file;//缓存文件夹
	
	private Thread downThread; //下载线程
	private Thread playerThread;//播放线程
	
	public MediaPlayerRobot(Context context){
		this.mContext = context;
		this.player = new MediaPlayer();
		this.player.setOnCompletionListener(this);
		this.cache_file = context.getCacheDir();
	}
	
	public MediaPlayer getPlayer() {
		return player;
	}
	
	public RingObj getRing() {
		return ring;
	}

	public MediaPlayerRobot setRing(RingObj ring) {
		this.ring = ring;
		if(mediaId == null){
			this.mediaId = ring.getRid();
		}
		if(mediaUrl == null){
			this.mediaUrl = ring.getDownUrl();
		}
		return this;
	}

	/**
	 * 下载网络铃声到本地
	 * */
	public void downNetRing() {
		String url = ring.getDownUrl();
		String rid = ring.getRid();
		String name = ring.getName();
		Handler mHandler = handler;
		Log.e("TAG", "铃声：" + name + " 开始下载...");
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		int failCount = 1;
		do {
			try {
				HttpResponse response = client.execute(get);
				HttpEntity entity = response.getEntity();
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK
						&& entity != null) {
					File file = new File(cache_file, rid + ".dat");
					
					if(file.exists()){
						file.delete();
					}

					InputStream inStream = entity.getContent();
					FileOutputStream outStream = new FileOutputStream(file);
					long length = entity.getContentLength();
					// 显示文件大小格式：1个小数点显示
					DecimalFormat df = new DecimalFormat("0.0");
					// 总文件大小
					//String fileSize = df.format((float) length / 1024 / 1024) + "MB";
					//缓存
					byte[] buffer = new byte[1024];
					int len = 0;
					int count = 0;
					String processText;
					long t = System.currentTimeMillis();
					
					while ((len = inStream.read(buffer)) != -1) {
						outStream.write(buffer, 0, len);
						// 下载进度
						count += len;
						processText = df.format((float) count*100 / length) + "%";
						Log.e("TAG", name);
						if(mHandler == handler){
							Message msg = new Message();
							msg.what = MEDIA_LOADING_PROCESS_UPDATE;
							msg.obj = processText;
							mHandler.sendMessage(msg);
						} else {
							break;
						}
					}
					inStream.close();
					outStream.close();
					
					Log.e("TAG", name + "下载完成，耗时：" + (System.currentTimeMillis() - t) + "毫秒");

					//将缓存文件转化为aac文件
					File aacFile = new File(cache_file, rid + ".aac");
					file.renameTo(aacFile);
					
					//播放文件
					if(ring.getRid().equals(rid)){
						playNow(aacFile);
					}
				}
				break;
			} catch (Exception e) {
				e.printStackTrace();
				failCount++;
				Log.e("TAG", "对于铃声" + name + "第" + failCount
						+ "次下载失败,正在尝试重新下载...");
			}
		} while (failCount < MAX_FAILCOUNT);
	}

	
	private void playNow(File media) {
		try {
			FileInputStream fis = new FileInputStream(media);
			player.setDataSource(fis.getFD());
			player.prepare();
			player.setAudioStreamType(AudioManager.STREAM_MUSIC);
			player.start();
			fis.close();
			handler.sendEmptyMessage(MEDIA_START);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class PlayerThread implements Runnable {
		
		private File media;
		public PlayerThread(File media){
			this.media = media;
		}
		@Override
		public void run() {
			playNow(media);
		}
	}
	public void reStart(){
		player.seekTo(pausePos);
		player.start();
		isPause = false;
	}
	public void tiggerMediaPlay() {
		if(isPause && pausePos > 0 && ring != null){
			Log.e("TAG", "pausePos" + pausePos);
			reStart();
			return;
		}
		File media = new File(cache_file, ring.getRid() + ".aac");
		if(!media.exists()) { //是否已经下载过
			Log.e("TAG", "loading thread start。。。");
			downThread = new Thread(){
				@Override
				public void run() {
					handler.sendEmptyMessage(MEDIA_LOADING);
					downNetRing();
				}
			};
			downThread.start();
		} else {
			Log.e("TAG", "player thread start...");
			playerThread = new Thread(new PlayerThread(media));
			playerThread.start();
		}
	}
	public MediaPlayerRobot reset(Handler mHandler) {
		if(downThread != null){
			handler.sendEmptyMessage(MEDIA_RESET);
			downThread.interrupt();
		}
		if(playerThread != null){
			playerThread.interrupt();
		}
		
		
		pausePos = 0;
		mediaId = null;
		mediaUrl = null;
		ring = null;
		isPause = false;
		player.reset();
		handler = mHandler;
		return this;
	}
	public void tiggerMediaPause() {
		Log.e("TAG", "tigger media pause。。。");
		if(player.isPlaying()){
			player.pause();
			pausePos = player.getCurrentPosition();
			isPause = true;
		}
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		handler.sendEmptyMessage(MEDIA_END);
		player.reset();
	}
}
