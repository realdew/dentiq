package dentiq.api.model;

import java.util.List;
import java.util.Map;

public class OrderedMap<T> {
	
	private Map<String, T> map;
	
	private List<String> keyList;
	
	public T get(String key) {
		return this.map.get(key);
	}
	
	public T get(int index) {
		String key = this.keyList.get(index);
		return this.map.get(key);
	}
	
	public void add(int index, String key, T val) {
		this.map.put(key, val);
		if ( !this.keyList.contains(key) ) this.keyList.add(index, key);
		// 만약에 동일한 key가 이미 존재한다면, keyList에 변경 없음.
	}
	public void add(String key, T val) {
		this.map.put(key, val);
		if ( !this.keyList.contains(key) ) this.keyList.add(key);
	}
	
	public void sortByKey() {
		
	}
	
	public synchronized void sortByValue() {
		// Comparable이 아니면, SKIP
		
		//this.map.
		
	}
	
	
	

}
