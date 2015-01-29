<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<!-- Check for organizational models. Without those, the application cannot work! -->

<h2>
<bean:message key="label.protocols.alerts" bundle="PROTOCOLS_RESOURCES" />
</h2>

<div align="center" style="font-weight:bold;">
	<html:errors bundle="PROTOCOLS_RESOURCES"/>
	<html:messages id="message" name="success" message="true" bundle="PROTOCOLS_RESOURCES">
		<bean:write name="message" />
	</html:messages>
</div>

<table width="100%">

<tr>

<td width="50%" valign="top" align="center">

<logic:notEmpty name="almostExpiredProtocols">
<div class="infobox">
	<p class="dinline"><strong><bean:message key="label.alerts.almostExpired" bundle="PROTOCOLS_RESOURCES" /></strong></p>
</div>
	<fr:view name="almostExpiredProtocols" schema="show.almostExpiredProtocols">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight"/>
			<fr:property name="columnClasses" value="acenter,acenter,acenter,"/>
			<fr:property name="link(renew)" value="/protocolAlerts.do?method=prepareRenewProtocol" />
			<fr:property name="key(renew)" value="link.protocols.renew" />
			<fr:property name="param(renew)" value="OID/protocolOID" />
			<fr:property name="bundle(renew)" value="PROTOCOLS_RESOURCES" />
		</fr:layout>
	</fr:view> 
</logic:notEmpty>

</td>
<td width="50%" valign="top" align="center">

<logic:notEmpty name="nullEndDateProtocols">
<div class="infobox">
	<p class="dinline"><strong><bean:message key="label.alerts.nullEndDates" bundle="PROTOCOLS_RESOURCES" /></strong></p>
</div>
	<fr:view name="nullEndDateProtocols" schema="show.nullEndDateProtocols">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight"/>
			<fr:property name="columnClasses" value="acenter,acenter,acenter,"/>
			<fr:property name="link(editDates)" value="/protocolAlerts.do?method=prepareEditProtocolHistory" />
			<fr:property name="key(editDates)" value="link.protocols.editDates" />
			<fr:property name="param(editDates)" value="OID/OID" />
			<fr:property name="bundle(editDates)" value="PROTOCOLS_RESOURCES" />
		</fr:layout>
	</fr:view> 
</logic:notEmpty>

</td>
</tr>
</table>

<logic:empty name="almostExpiredProtocols">
<logic:empty name="nullEndDateProtocols">

<div class="infobox" align="center">
	<p class="dinline"><strong><bean:message key="label.protocols.noAlerts" bundle="PROTOCOLS_RESOURCES"/></strong></p>
</div>
	
</logic:empty>
</logic:empty>
