package dentiq.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import dentiq.api.repository.TestMapper;


/**
 * 로그인을 성공한 후에만 사용할 수 있는 API
 * @author lee
 *
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins="*")
public class SecuredController {

	
	private static final Logger logger = LoggerFactory.getLogger(NormalController.class);
	
	
	@RequestMapping(value="/greeting/2", method=RequestMethod.GET)
	public String greeting() {
		return "Hello!";
	}
	
	@Autowired
	TestMapper testMapper;
	
	
	@RequestMapping(value="/test2", method=RequestMethod.GET)
	public Long[] doTest() {
		return testMapper.listAll();
	}
}
