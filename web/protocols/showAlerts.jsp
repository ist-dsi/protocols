<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<!-- Check for organizational models. Without those, the application cannot work! -->

<div align="center" style="font-weight:bold;">
	<html:errors bundle="PROTOCOLS_RESOURCES"/>
	<html:messages id="message" name="success" message="true" bundle="PROTOCOLS_RESOURCES">
		<bean:write name="message" />
	</html:messages>
</div>

<logic:notPresent name="internalOrganizationalModel">

	<div class="error1">
		<br />
		<bean:message key="label.organizationalModel.internal.undefined" bundle="PROTOCOLS_RESOURCES"/>
		<br />
		<p><html:link href="protocols.do?method=prepareToDefineOrganizationalModel"><bean:message key="link.organizationalModel.define" bundle="PROTOCOLS_RESOURCES"/></html:link></p>
	</div>
	
</logic:notPresent>


<logic:notPresent name="externalOrganizationalModel">

	<div class="error1">
		<br />
		<bean:message key="label.organizationalModel.external.undefined" bundle="PROTOCOLS_RESOURCES"/>
		<br />
		<p><html:link href="protocols.do?method=prepareToDefineOrganizationalModel"><bean:message key="link.organizationalModel.define" bundle="PROTOCOLS_RESOURCES"/></html:link></p>
	</div>
	
</logic:notPresent>

<logic:present name="internalOrganizationalModel">
<logic:present name="externalOrganizationalModel">

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
			<fr:property name="link(renew)" value="/protocols.do?method=prepareRenewProtocol" />
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
			<fr:property name="link(editDates)" value="/protocols.do?method=prepareEditProtocolHistory" />
			<fr:property name="key(editDates)" value="link.protocols.editDates" />
			<fr:property name="param(editDates)" value="OID/OID" />
			<fr:property name="bundle(editDates)" value="PROTOCOLS_RESOURCES" />
		</fr:layout>
	</fr:view> 
</logic:notEmpty>

</td>
</tr>
</table>

</logic:present>
</logic:present>

<logic:empty name="almostExpiredProtocols">
<logic:empty name="nullEndDateProtocols">

<div class="infobox" align="center">
	<p class="dinline"><strong><bean:message key="label.protocols.noAlerts" bundle="PROTOCOLS_RESOURCES"/></strong></p>
</div>
	
</logic:empty>
</logic:empty>