package com.hblg.lookingfellow.tools;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class StreamTool {

	public static byte[] read(InputStream inputStream) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int len = 0;
		while((len=inputStream.read(buf)) != -1) {
			baos.write(buf, 0, len);
		}
		return baos.toByteArray();
	}

}
