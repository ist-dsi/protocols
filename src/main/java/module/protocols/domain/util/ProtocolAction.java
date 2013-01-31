package module.protocols.domain.util;

import java.io.Serializable;
import java.util.EnumSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class ProtocolAction implements Serializable {

	private static final long serialVersionUID = -3092267605064012461L;

	private final EnumSet<ProtocolActionType> protocolActionTypes;

	private final String otherTypes;

	public ProtocolAction(List<ProtocolActionType> protocolActionTypes, String otherTypes) {
		EnumSet<ProtocolActionType> actionTypes = EnumSet.noneOf(ProtocolActionType.class);
		for (ProtocolActionType actionType : protocolActionTypes) {
			actionTypes.add(actionType);
		}
		this.protocolActionTypes = actionTypes;
		this.otherTypes = otherTypes;
	}

	public ProtocolAction(EnumSet<ProtocolActionType> protocolActionTypes, String otherTypes) {
		this.protocolActionTypes = protocolActionTypes;
		this.otherTypes = otherTypes;
	}

	public String getOtherTypes() {
		return otherTypes;
	}

	public EnumSet<ProtocolActionType> getProtocolActionTypes() {
		return protocolActionTypes;
	}

	public boolean contains(List<ProtocolActionType> protocolActionTypes) {
		return getProtocolActionTypes().containsAll(protocolActionTypes);
	}

	public boolean contains(ProtocolActionType protocolActionType) {
		return getProtocolActionTypes().contains(protocolActionType);
	}

	private static final String protocolActionTypeSeparator = ",";
	private static final String protocolActionStringSeparator = ";";

	@Override
	public String toString() {
		StringBuilder dest = new StringBuilder();
		for (ProtocolActionType protocolActionType : getProtocolActionTypes()) {
			if (dest.length() != 0) {
				dest.append(protocolActionTypeSeparator);
			}
			dest.append(protocolActionType.name());
		}

		dest.append(protocolActionStringSeparator);
		if (!StringUtils.isEmpty(getOtherTypes())) {
			dest.append(getOtherTypes());
		}
		return dest.toString();
	}

	public static ProtocolAction fromString(String extRep) {
		String[] tokens = extRep.split(protocolActionStringSeparator, 2);
		EnumSet<ProtocolActionType> protocolsActionsTypes = EnumSet.noneOf(ProtocolActionType.class);
		for (String enumTokens : tokens[0].split(protocolActionTypeSeparator)) {
			if (!StringUtils.isEmpty(enumTokens)) {
				protocolsActionsTypes.add(ProtocolActionType.valueOf(enumTokens));
			}
		}
		return new ProtocolAction(protocolsActionsTypes, tokens[1].length() != 0 ? tokens[1] : null);
	}
}
