package dentiq.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 업무 관리용 API 컨트롤러
 * 
 * @author			leejuhyeon@gmail.com
 * @lastUpdated		2017.12.26
 */
@RestController
@RequestMapping("/management_api")
@CrossOrigin(origins="*")		//TODO 향후에 CrossOrigin는 막아야 할 필요 있음
public class ManagementController {
	
	private static final Logger logger = LoggerFactory.getLogger(NormalController.class);
	
	
}
