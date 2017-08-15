

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.ehcache.Cache;
import org.ehcache.Cache.Entry;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;


public class Main {
	
	private static final int trezentosMil = 300000;
	private static final int cemMil = 30000000;
	
	private void runCompare() {
		List<String> searches = new ArrayList<>();
		Map<String, String> mapTest = new HashMap<>();
		
		CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder() 
			    .withCache("preConfigured",CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, String.class, ResourcePoolsBuilder.heap(trezentosMil))) 
			    .build(); 
		cacheManager.init(); 
		
		Cache<String, String> cache = cacheManager.createCache("myCache", CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, String.class, ResourcePoolsBuilder.heap(trezentosMil)));

		String keyConst = "KeyName";
		String valueConst = "Val";
		
		
		String k;
		String v;
		int num;
		Map<String, String> list = getSeeds(keyConst, valueConst);
		mapTest.putAll(list);
		cache.putAll(list);
		
		searches = createSearchLists(keyConst);
		
		searchInMap(searches, mapTest);
		
		searchInEhCache(searches, cache);		
		
	}

	
	private int countCacheItems(Cache<String, String> cache) {
		Iterator<Entry<String, String>> it = cache.iterator();
		HashMap<String, String> hash = new HashMap<>();
		while (it.hasNext()) {
			Entry<String, String> k = it.next();
			String v = cache.get(k.getKey());
			if (v != null) {
				hash.put(k.getKey(), v);
			}
		}
		return hash.size();	
	}
	
	private void searchInEhCache(List<String> searches, Cache<String, String> cache) {
		String k;
		String v;
		System.out.println("Pesquisando no EhCache as "+Instant.now());		
		for (Iterator iterator = searches.iterator(); iterator.hasNext();) {
			k = (String) iterator.next();
			v = cache.get(k);
		}		
		System.out.println("Finalizou as "+Instant.now());
		System.out.println("Cache count: "+countCacheItems(cache));
	}

	private void searchInMap(List<String> searches, Map<String, String> mapTest) {
		String k;
		String v;
		System.out.println("Pesquisando no map as "+Instant.now());
		for (Iterator iterator = searches.iterator(); iterator.hasNext();) {
			k = (String) iterator.next();
			v = mapTest.get(k);
		}
		
		System.out.println("Finalizou as "+Instant.now());
		System.out.println(mapTest.size());
	}

	private List<String> createSearchLists(String keyConst) {
		Random r = new Random();
		System.out.println("criando lista de pesquisas  as "+Instant.now());
		List<String> result = new ArrayList<>();
		for (int i = 0; i < cemMil; i++) {			
			result.add(keyConst+r.nextInt(cemMil));
		} 
		System.out.println("Finalizou as "+Instant.now());
		return result;
	}

	private Map<String, String> getSeeds(String keyConst, String valueConst) {
		String k;
		String v;
		int num;
		Random r = new Random();
		Map<String, String> result = new HashMap<>();
		System.out.println("iniciando sementes as "+Instant.now());
		for (int i = 0; i < trezentosMil; i++) {
			num = r.nextInt(trezentosMil);
			k = keyConst+num;
			v = valueConst+num;
			result.put(k, v);			
		}
		System.out.println("Finalizou as "+Instant.now());
		return result;
	};
	
	public static void main(String[] args) {
		Main main = new Main();		
		main.runCompare();
	} 

}
