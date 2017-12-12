/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/
package com.pega.gcs.fringecommon.utilities.kyro;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.pega.gcs.fringecommon.log4j2.Log4j2Helper;

import net.jpountz.lz4.LZ4BlockInputStream;
import net.jpountz.lz4.LZ4BlockOutputStream;

public class KryoSerializer {

	private static final Log4j2Helper LOG = new Log4j2Helper(KryoSerializer.class);

	private static final ThreadLocal<Kryo> kryoThreadLocal = new ThreadLocal<Kryo>() {

		@Override
		protected Kryo initialValue() {
			Kryo kryo = new Kryo();
			return kryo;
		}

	};

	public static byte[] compress(Object object) throws Exception {

		byte[] byteArray = null;

		if (object != null) {

			// Serialise to a byte array
			try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

				try (LZ4BlockOutputStream lz4bos = new LZ4BlockOutputStream(baos); Output output = new Output(lz4bos)) {

					Kryo kryo = kryoThreadLocal.get();
					kryo.register(object.getClass());
					kryo.writeObject(output, object);

				} catch (Exception e) {
					LOG.error("Error compressing object", e);
					throw e;
				} finally {
					baos.close();
					byteArray = baos.toByteArray();
				}
			}
		} else {
			LOG.info("compress Object is null");
		}

		LOG.debug("Object compressed to: " + byteArray.length);

		return byteArray;
	}

	public static <T> T decompress(byte[] serialized, Class<T> clazz) throws Exception {

		T object = null;

		if (serialized != null) {

			// deserialize from a byte array
			try (ByteArrayInputStream bais = new ByteArrayInputStream(serialized);
					LZ4BlockInputStream lz4bis = new LZ4BlockInputStream(bais);
					Input input = new Input(lz4bis)) {

				Kryo kryo = kryoThreadLocal.get();
				object = kryo.readObject(input, clazz);

			} catch (Exception e) {
				LOG.error("Error decompressing object", e);
				throw e;
			}
		} else {
			LOG.info("decompress Object is null");

		}

		return object;
	}
}
