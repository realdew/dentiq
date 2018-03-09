package dentiq.api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class NameCountPair {
	
	@Getter @Setter	
	private String name;
	
	@Getter @Setter
	private Long cnt;
	
	public NameCountPair(String name, Long cnt) {
		this.name = name;
		this.cnt = cnt;
	}

}
