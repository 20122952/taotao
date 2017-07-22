package com.taotao.fastdfs;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

public class TestFastDFS {
	
	@Test
	public void uploadFile() throws Exception{
		
		
	}
	public static void main(String[] args) {
		try {
			ClientGlobal.init("F:/Project/eclipse2/workspace/taotao-manager-web/src/main/resources/resource/client.conf");
			TrackerClient trackerClient = new TrackerClient();
			TrackerServer trackerServer = trackerClient.getConnection();
			StorageServer storageServer = null;
			StorageClient storageClient = new StorageClient(trackerServer,storageServer);
			String[] strings = storageClient.upload_file("D:/Pic/a.jpg","jpg",null);
			for (String string : strings) {
				System.out.println(string);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
