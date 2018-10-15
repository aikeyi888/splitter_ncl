package com.wisdom.ncl.splitter.tools;

import java.io.Serializable;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * 
 * ����˵��: ���湤���� ����: �ں� ����ʱ��: 2012-3-30 ����10:59:57 ����޸�ʱ��: 2012-3-30 ����10:59:57
 * ����޸���: �ں� �汾 1.0
 */
public class EhcacheTools {

	public static CacheManager manager;

	static {
		try {
			manager = CacheManager.getInstance();
			if (manager == null)
				manager = CacheManager.create();
		} catch (CacheException e) {

		}
	}

	/**
	 * �ӻ����л�ȡ����
	 * 
	 * @param cache_name
	 * @param key
	 * @return
	 */
	public static Object getObjectCached(String cache_name, Object key) {
		Cache cache = getCache(cache_name);
		if (cache != null) {
			try {
				Element elem = cache.get(key);
				if (elem != null && !cache.isExpired(elem))
					return elem.getValue();
			} catch (Exception e) {
			}
		}
		return null;
	}

	/**
	 * �Ѷ�����뻺����
	 * 
	 * @param cache_name
	 * @param key
	 * @param value
	 */
	public synchronized static void putObjectCached(String cache_name,
			Object key, Object value) {
		Cache cache = getCache(cache_name);
		if (cache != null) {
			try {
				cache.remove(key);
				Element elem = new Element(key, value);
				cache.put(elem);
			} catch (Exception e) {
			}
		}
	}

	/**
	 * ��ȡָ�����ƵĻ���
	 * 
	 * @param arg0
	 * @return
	 * @throws IllegalStateException
	 */
	public static Cache getCache(String arg0) throws IllegalStateException {
		return manager.getCache(arg0);
	}

	/**
	 * ��ȡ�����е���Ϣ
	 * 
	 * @param cache
	 * @param key
	 * @return
	 * @throws IllegalStateException
	 * @throws CacheException
	 */
	public static Element getElement(String cache, Object key)
			throws IllegalStateException, CacheException {
		Cache cCache = getCache(cache);
		return cCache.get(key);
	}

	/**
	 * ֹͣ���������
	 */
	public static void shutdown() {
		if (manager != null)
			manager.shutdown();
	}

}
