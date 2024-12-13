package vttp.batch5.ssf.noticeboard.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import vttp.batch5.ssf.noticeboard.constant.Constant;
import vttp.batch5.ssf.noticeboard.models.NoticeResponse;

@Repository
public class NoticeRepository {

	@Autowired
	private RedisTemplate<String, Object> template;

	// TODO: Task 4
	// You can change the signature of this method by adding any number of parameters
	// and return any type
	// 
	/*
	 * Write the redis-cli command that you use in this method in the comment. 
	 * For example if this method deletes a field from a hash, then write the following
	 * redis-cli command 
	 * 	hdel myhashmap a_key
	 *
	 *
	 */
	public void insertNotices(NoticeResponse noticeResponse) {
		// convert back to json?
		JsonObject jsonObject = Json.createObjectBuilder()
								.add("id", noticeResponse.getId())
								.add("timestamp", noticeResponse.getTimestamp())
								.build();

		// lets use a map?
		// hset successNotices 01JEYYGMKGR1ZKGP95VF5ZR7C0 "{\"id\":\"01JEYYGMKGR1ZKGP95VF5ZR7C0\",\"timestamp\":\"1734057939568\"}"
		template.opsForHash().put(Constant.redisKey, noticeResponse.getId(), jsonObject.toString());


	}

	public String retrieveRandomKey() {
		// randomkey
		return template.randomKey();
	}


}
