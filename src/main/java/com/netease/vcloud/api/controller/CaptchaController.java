package com.netease.vcloud.api.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.annotation.Resource;
import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.testng.log4testng.Logger;

import com.netease.vcloud.storage.cache.IRedisCached;
import com.netease.vcloud.utils.Constants;
import com.netease.vcloud.utils.SerializeUtil;

/**
 * @author hzgaochao
 * @version 创建时间：Sep 8, 2015 输出验证码
 */
@Controller
public class CaptchaController {
	private static final HttpHeaders HTTP_HEADERS;
	private static final Random RANDOM;
	private static Logger logger = Logger.getLogger(CaptchaController.class);

	@Resource(name = "redisCachedImpl")
	private IRedisCached redisCached;

	static {
		HTTP_HEADERS = new HttpHeaders();
		HTTP_HEADERS.set("Pragma", "No-cache");
		HTTP_HEADERS.set("Cache-Control", "No-cache");
		HTTP_HEADERS.setContentType(MediaType.IMAGE_JPEG);
		RANDOM = new Random();
	}

	private int width;
	private int height;

	@RequestMapping(value = "/live/captcha/{captcha_id}", method = RequestMethod.GET)
	public ResponseEntity<byte[]> captcha(@PathVariable String captcha_id) throws IOException {

		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();
		g.setColor(getRandColor(200, 250));
		g.fillRect(1, 1, width - 1, height - 1);
		g.setColor(new Color(102, 102, 102));
		g.drawRect(0, 0, width - 1, height - 1);
		g.setFont(new Font("Times New Roman", Font.BOLD, 17));
		g.setColor(getRandColor(160, 200));

		// 画随机线
		for (int i = 0; i < 155; i++) {
			int x = RANDOM.nextInt(width - 1);
			int y = RANDOM.nextInt(height - 1);
			int xl = RANDOM.nextInt(6) + 1;
			int yl = RANDOM.nextInt(12) + 1;
			g.drawLine(x, y, x + xl, y + yl);
		}

		// 从另一方向画随机线
		for (int i = 0; i < 70; i++) {
			int x = RANDOM.nextInt(width - 1);
			int y = RANDOM.nextInt(height - 1);
			int xl = RANDOM.nextInt(12) + 1;
			int yl = RANDOM.nextInt(6) + 1;
			g.drawLine(x, y, x - xl, y - yl);
		}

		// 生成随机数,并将随机数字转换为字母
		String captchaStr = "";
		for (int i = 0; i < 6; i++) {
			int itmp = RANDOM.nextInt(26) + 97;
			char ctmp = (char) itmp;
			captchaStr += String.valueOf(ctmp);
			g.setColor(new Color(20 + RANDOM.nextInt(110), 20 + RANDOM.nextInt(110), 20 + RANDOM.nextInt(110)));
			g.drawString(String.valueOf(ctmp), 15 * i + 10, 25);
		}
		g.dispose();

		// 保存验证码到redis
		try {
			redisCached.updateCached(SerializeUtil.serialize(Constants.LOGIN_CAPTCHA + captcha_id), SerializeUtil.serialize(captchaStr), 120L);
		} catch (Exception e) {
			logger.error("save captcha to redis failed!>>id:" + captcha_id);
			e.printStackTrace();
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ImageIO.write(image, "JPEG", out);

		try {
			return new ResponseEntity<byte[]>(out.toByteArray(), HTTP_HEADERS, HttpStatus.OK);
		} finally {
			IOUtils.closeQuietly(out);
		}
	}

	private Color getRandColor(int fc, int bc) {
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + RANDOM.nextInt(bc - fc);
		int g = fc + RANDOM.nextInt(bc - fc);
		int b = fc + RANDOM.nextInt(bc - fc);
		return new Color(r, g, b);
	}

	// access method
	// ----------------------------------------------------------------------------------------------------------------------

	@Value("100")
	public void setWidth(int width) {
		this.width = width;
	}

	@Value("35")
	public void setHeight(int height) {
		this.height = height;
	}
}
