/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/
package com.pega.gcs.fringecommon.utilities;

/**
 * This class provides methods to encode byte-sequences into BASE64 encoded
 * strings and decode BASE64 encoded strings into byte-sequences. The BASE64
 * encoding is designed to represent arbitrary sequences of bytes in a form that
 * is human readable. During encoding 3 (8-bit) groups of input bytes are
 * converted to 4 (6-bit) output groups. Each of these 6-bit groups is
 * translated to a single character / digit / symbol in the base 64 alphabet.
 * <p>
 * Consider the following example where 3 bytes, X, Y, Z are converted to 4 base
 * 64 characters, A, B, C, D,
 * 
 * <pre>
 * input:  XXXXXXXX YYYYYYYY ZZZZZZZZ
 * output: AAAAAABB BBBBCCCC CCDDDDDD
 * </pre>
 * 
 * Note that A, B, C, D are variables and their values depend on the bits in X,
 * Y, Z. B, for instance, is represented by concatenating the last two bits of X
 * with the first 4 bits of Y. That gives us a 6-bit number. This number is used
 * as an index in the base 64 alphabet to convert it into a character.
 * <p>
 * When the length of the input bytes is not a multiple of 3, we use padding
 * characters (the equals sign). As a consequence, all valid BASE64 encoded
 * strings have a length that is a multiple of 4.
 * <p>
 * Note also that when we mention BASE64 encoded strings, we refer to strings
 * that contain only characters from the base 64 alphabet.
 * <p>
 * According to the definition of BASE64 encoding the output must be represented
 * in lines of no more than 76 characters each. The methods in this class ignore
 * this and flag an error when they encounter any whitespace in the strings.
 * 
 * @see <a href="http://rfc.net/rfc1521.html#s5.2.">RFC1521</a>
 */
public class Base64 {
	/**
	 * This exception can be thrown during the decoding process when the input
	 * contains illegal characters.
	 */
	public static final class FormatException extends RuntimeException {

		private static final long serialVersionUID = 1;

		/**
		 * Constructs an <code>FormatException</code> with the specified detail
		 * message.
		 * 
		 * @param msg
		 *            the detail message.
		 */
		public FormatException(String msg) {
			super(msg);
		}
	}

	/**
	 * Mapping from 6-bit indexes to 6-bit Base64 characters:
	 * 
	 * <pre>
	 * [0, 64) -> [A-Ba-b0-9+/]
	 * </pre>
	 */
	private static final char[] _B2C = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
			'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
			'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', '+', '/' };

	/** The padding character for the BASE64 encoding */
	private static final char PAD = '=';

	/** Represents invalid characters */
	private static final byte XX = -1;

	/** Represents (ASCII) white space characters */
	private static final byte WS = -2;

	/**
	 * Mapping from 6-bit Base64 characters to 6-bit indexes:
	 * 
	 * <pre>
	 * [A-Ba-b0-9+/] -> [0, 64)
	 * </pre>
	 */
	private static final byte[] _C2B = { XX, XX, XX, XX, XX, XX, XX, XX, XX, WS, WS, XX, XX, WS, XX, XX, XX, XX, XX, XX,
			XX, XX, XX, XX, XX, XX, XX, XX, XX, XX, XX, XX, WS, XX, XX, XX, XX, XX, XX, XX, XX, XX, XX, 62, XX, XX, XX,
			63, // 62: +, 63: /
			52, 53, 54, 55, 56, 57, 58, 59, 60, 61, XX, XX, XX, XX, XX, XX, // 52-61:
			// 0
			// -
			// 9
			XX, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, // 0-14: A - O
			15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, XX, XX, XX, XX, XX, // 15-25:
			// P
			// -
			// Z
			XX, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, // 26-40:
			// a
			// -
			// o
			41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, XX, XX, XX, XX, XX // 41-51:
			// p
			// -
			// z
	};

	/**
	 * Convert 6-bits index to 6-bits character.
	 * 
	 * @param idx
	 *            a number in the range [0, 63)
	 * @return the corresponding character from [A-Ba-b0-9+/]
	 */
	private static char B2C(int idx) {
		return _B2C[idx];
	}

	/**
	 * Convert a BASE64 encoded character to 6-bits index. Note that when the
	 * character is not a BASE64 encoding character, this method returns a
	 * negative index.
	 * 
	 * @param ch
	 *            a character from [A-Ba-b0-9+/]
	 * @return the index of the character or a negative index when the input is
	 *         invalid
	 */
	private static byte C2B(char ch) {
		return ch < _C2B.length ? _C2B[ch] : XX;
	}

	/**
	 * Encode the specified bytes using BASE64 encoding.
	 * 
	 * @param bytes
	 *            arbitrary sequence of bytes
	 * @return BASE64 representation of the input bytes
	 */
	public static String encode(byte[] bytes) {
		return encode(bytes, 0, bytes.length);
	}

	/**
	 * Given an offset an number of bytes, encode the specified sequence of
	 * bytes using BASE64 encoding.
	 * 
	 * @param bytes
	 *            arbitrary sequence of bytes
	 * @param offset
	 *            the offset in the bytes array
	 * @param len
	 *            the number of bytes to encode
	 * @return BASE64 representation of the input bytes
	 */
	public static String encode(byte[] bytes, int offset, int len) {
		int len43 = 4 * (len / 3) + ((len % 3) > 0 ? 4 : 0);
		char[] ch = new char[len43];
		int i = 0, j = 0;
		while (i < len) {
			int k = offset + i;
			int rem = len - i;
			byte x = bytes[k];
			byte y = rem > 1 ? bytes[k + 1] : 0;
			byte z = rem > 2 ? bytes[k + 2] : 0;
			ch[j++] = B2C(x >>> 2 & 0x3F);
			ch[j++] = B2C((x << 4 & 0x30) | (y >>> 4 & 0xF));
			ch[j++] = rem > 1 ? B2C((y << 2 & 0x3C) | (z >>> 6 & 0x3)) : PAD;
			ch[j++] = rem > 2 ? B2C(z & 0x3F) : PAD;
			i += 3;
		}
		return String.valueOf(ch);
	}

	/**
	 * Decodes BASE64 encode-strings into their original byte sequences.
	 * 
	 * @param str
	 *            the BASE64 encoded version of the data
	 * @return the sequence of bytes that was encoded by the input string
	 */
	public static byte[] decode(String str) {
		char[] ch = str.toCharArray();
		return decode(ch);
	}

	/**
	 * Decodes BASE64 encode characters into their original byte sequences.
	 * 
	 * @param ch
	 *            the BASE64 encoded version of the data
	 * @return the sequence of bytes that was encoded by the input character
	 *         array
	 */
	public static byte[] decode(char[] ch) {
		return decode(ch, 0, ch.length);
	}

	/**
	 * Given an offset and a number of characters, decode the BASE64 characters
	 * into their original byte sequences.
	 * 
	 * @param ch
	 *            the BASE64 encoded version of the data
	 * @param offset
	 *            the offset in the characters array
	 * @param len
	 *            the number of characters to decode
	 * @return the sequence of bytes that was encoded by the input character
	 *         array
	 * @exception IllegalArgumentException
	 *                when <code>len</code> is not a multiple of 4
	 * @exception FormatException
	 *                when the input contains illegal characters
	 */
	public static byte[] decode(char[] ch, int offset, int len) {
		if (len % 4 > 0)
			throw new IllegalArgumentException("Length must be a multiple of 4");
		int npad = 0;
		int last = offset + len - 1;
		while (last >= 0 && ch[last--] == PAD)
			npad++;
		if (npad > 2)
			throw new FormatException("Too many padding characters");
		int len34 = 3 * (len / 4) - npad;
		byte[] bytes = new byte[len34];
		int i = 0, j = 0;
		while (i < len) {
			int k = offset + i;
			byte a = C2B(ch[k]);
			byte b = C2B(ch[k + 1]);
			byte c = ch[k + 2] == PAD ? 0 : C2B(ch[k + 2]);
			byte d = ch[k + 3] == PAD ? 0 : C2B(ch[k + 3]);
			if (a < 0 || b < 0 || c < 0 || d < 0)
				throw new FormatException("Illegal character(s) in " + new String(ch, k, 4));
			if ((ch[k + 2] == PAD && ch[k + 3] != PAD) || (ch[k + 3] == PAD && k + 4 < len))
				throw new FormatException("Padding character(s) not at the end");
			bytes[j++] = (byte) (a << 2 & 0xFC | b >>> 4 & 0x3);
			if (ch[k + 2] != PAD)
				bytes[j++] = (byte) (b << 4 & 0xF0 | c >>> 2 & 0xF);
			if (ch[k + 3] != PAD)
				bytes[j++] = (byte) (c << 6 & 0xC0 | d & 0x3F);
			i += 4;
		}
		return bytes;
	}
}
