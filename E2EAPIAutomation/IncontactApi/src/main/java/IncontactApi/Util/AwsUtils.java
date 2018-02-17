package IncontactApi.Util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class AwsUtils {

	public static String currdate;
	public static String currentTimeStamp;
	private static byte[] HmacSHA256(String data, byte[] key) throws Exception {
		String algorithm = "HmacSHA256";
		Mac mac = Mac.getInstance(algorithm);
		mac.init(new SecretKeySpec(key, algorithm));
		return mac.doFinal(data.getBytes("UTF8"));
	}

	private static String generateHex(String data) {
		MessageDigest messageDigest;
		try {
			messageDigest = MessageDigest.getInstance("SHA-256");
			messageDigest.update(data.getBytes("UTF-8"));
			byte[] digest = messageDigest.digest();
			return String.format("%064x", new java.math.BigInteger(1, digest));
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static byte[] getSignatureKey(String key, String dateStamp, String regionName, String serviceName) throws Exception {
		byte[] kSecret = ("AWS4" + key).getBytes("UTF8");
		byte[] kDate = HmacSHA256(dateStamp, kSecret);
		byte[] kRegion = HmacSHA256(regionName, kDate);
		byte[] kService = HmacSHA256(serviceName, kRegion);
		byte[] kSigning = HmacSHA256("aws4_request", kService);
		return kSigning;
	}

	public static String toHexNumber(byte[] data) {
		StringBuilder sb = new StringBuilder(data.length * 2);
		for (int i = 0; i < data.length; i++) {
			String hex = Integer.toHexString(data[i]);
			if (hex.length() == 1) {
				sb.append("0");
			} else if (hex.length() == 8) {
				hex = hex.substring(6);
			}
			sb.append(hex);
		}
		return sb.toString().toLowerCase(Locale.getDefault());
	}

	private static String getCurrentTimeStamp() {
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		return dateFormat.format(new Date());

	}

	private static String getCurrentDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		return dateFormat.format(new Date());

	}

	public static String generateSignature(String protocol, String uri, String path, String secretKey, String region,String serviceName) {
		String signature = null;
		try {
			currentTimeStamp = getCurrentTimeStamp();
			currdate = getCurrentDate();
			String contentType ="application/json";
			byte[] signingKey = getSignatureKey(secretKey, currdate, region, serviceName);
			String payloadhash = generateHex("");
			String canonicalURI = protocol.toUpperCase() + "\n" + path + "/\n\nhost:" + uri + "\nx-amz-date:" + currentTimeStamp + "\n\nhost;x-amz-date\n" + payloadhash;
			String stringtosign = "AWS4-HMAC-SHA256\n" + currentTimeStamp + "\n" + currdate + "/" + region + "/" + serviceName + "/aws4_request\n" + generateHex(canonicalURI);

			/*
			 * for(int i=0; i<value.length;i++){ System.out.print(value[i]); }
			 */
			byte[] byteSignature = HmacSHA256(stringtosign, signingKey);
			// System.out.println("Signature :"+ toHexNumber(signature));
//			timeStampSignature[0] = timedate;
			signature = toHexNumber(byteSignature);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return signature;

	}

	public static void main(String[] args) {
		String timestampSignature = generateSignature("GET", "2epm7cn73b.execute-api.us-west-2.amazonaws.com",
				"/dev/egressregistration/hutch/bpl", "S/g0Wamb6+ISslvuN71TNwZyd9giaHk3pyhS3R4P", "us-west-2",
				"execute-api");
		System.out.println(timestampSignature);
		System.out.println(currentTimeStamp);
//		String abc= "\n\napplication/json\nhost";
//		System.out.println(abc);
	}
}
