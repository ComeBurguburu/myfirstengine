package com.comeb;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.cache.CacheException;
import javax.cache.CacheFactory;
import javax.cache.CacheManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.jsr107cache.GCacheFactory;

public class MemoryCache extends HttpServlet{

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		javax.cache.Cache cache=null;
		Map props = new HashMap();
		props.put(GCacheFactory.EXPIRATION_DELTA, 3600);
		props.put(MemcacheService.SetPolicy.ADD_ONLY_IF_NOT_PRESENT, true);
		try {
			// R�cup�ration du Cache
			CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
			// cr�ation/r�cup�ration du cache suivant des propri�t�s sp�cifiques
			cache = cacheFactory.createCache(props);

			// Si aucune propri�t� n'est sp�cifi�e,
			//cr�er/r�cup�rer un cache comme ci-dessous
			//cache = cacheFactory.createCache(Collections.emptyMap());
		} catch (CacheException e) {
			// Traitement en cas d'erreur sur la r�cup�ration/configuration du cache
		}
		String key = "message";
		String value = null;
		// Mise en cache de l'objet � l'aide d'une cl�
		// cache.put(key, value);
		// R�cup�ration de l'objet stock�
		value = (String)cache.get(key);
		Date init = new Date();
		if(value==null){
			resp.getWriter().write("not in cache");
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			Query q = new Query("message");


			PreparedQuery pq=datastore.prepare(q);
			for(Entity result: pq.asIterable()){
				String r=(String)result.getProperty("message");
				cache.put(key,r);
				value  = r;
			}

		}else{
			resp.getWriter().write("in cache");
		}
		Date end = new Date();
		Long time_mili_secondes = end.getTime() - init.getTime();
		resp.getWriter().write(" value: ");
		resp.getWriter().write(value);
		resp.getWriter().write(" in " + time_mili_secondes+ "ms");
	}
}
