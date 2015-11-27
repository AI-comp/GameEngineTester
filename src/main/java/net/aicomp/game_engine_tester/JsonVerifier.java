package net.aicomp.game_engine_tester;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonVerifier {

	private String lastKey;
	private Object lastActualValue;
	private Object lastExpectedValue;

	public JsonVerifier() {
	}

	public boolean verify(File file, int aiCount) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			@SuppressWarnings("unchecked")
			Map<String, Object> gameResult = mapper.readValue(file, Map.class);
			return verify(gameResult);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	private boolean verify(Map<String, Object> gameResult) {
		if (!checkType(gameResult, "log", ArrayList.class)) {
			return false;
		}
		if (!checkValue(gameResult, "winner", -1)) {
			return false;
		}
		if (!checkType(gameResult, "replay", ArrayList.class) && !checkType(gameResult, "replay", Map.class)) {
			return false;
		}
		ArrayList<Map<String, Object>> list = (ArrayList<Map<String, Object>>) gameResult.get("log");
		if (list.size() > 0) {
			Map<String, Object> log = list.get(0);
			if (!checkType(log, "target", Integer.class)) {
				return false;
			}
			if (!checkType(log, "message", String.class)) {
				return false;
			}
		}
		return true;
	}

	public boolean checkType(Map<String, Object> map, String key, Class<?> klass) {
		lastKey = key;
		lastExpectedValue = klass;
		Object object = map.get(key);
		if (object == null) {
			return false;
		}
		lastActualValue = object.getClass();
		return klass.equals(lastActualValue);
	}

	public boolean checkValue(Map<String, Object> map, String key, Object value) {
		lastKey = key;
		lastExpectedValue = value;
		lastActualValue = map.get(key);
		return value.equals(lastActualValue);
	}

	public String getLastKey() {
		return lastKey;
	}

	public Object getLastActualValue() {
		return lastActualValue;
	}

	public Object getLastExpectedValue() {
		return lastExpectedValue;
	}
}
