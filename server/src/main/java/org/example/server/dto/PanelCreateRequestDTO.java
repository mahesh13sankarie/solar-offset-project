package org.example.server.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class PanelCreateRequestDTO {
	private String name;
	private Double installationCost;
	private Double productionPerPanel;
	private String description;
	private List<String> countryCodes;
}