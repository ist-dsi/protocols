<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<br />
<h2><bean:message key="label.protocols.details" bundle="PROTOCOLS_RESOURCES"/></h2>

<div align="center" style="font-weight:bold;">
	<html:errors bundle="PROTOCOLS_RESOURCES"/>
	<html:messages id="message" name="success" message="true" bundle="PROTOCOLS_RESOURCES">
		<bean:write name="message" />
	</html:messages>
</div>


<p>
<logic:present name="protocol">

<bean:define id="OID" type="java.lang.Long" name="protocol" property="OID"/>

<table width="100%">
<tr>
<td width="50%" valign="top">

<div class="infobox">
	<p class="dinline"><strong><bean:message key="label.protocols.data" bundle="PROTOCOLS_RESOURCES"/></strong></p>
</div>
	
<fr:view name="protocol" schema="show.protocol.data">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight thright"/>
		<fr:property name="columnClasses" value="aleft,aleft,,"/>
	</fr:layout>
</fr:view>

</td>

<td width="50%" valign="top">

<div class="infobox">
	<p class="dinline"><strong><bean:message key="label.protocols.files" bundle="PROTOCOLS_RESOURCES"/></strong></p>
</div>

<logic:present name="protocolFiles">
<logic:iterate id="file" name="protocolFiles">
	<bean:write name="file" property="name"/>
	<br />
</logic:iterate>
<logic:empty name="protocolFiles">
<div align="center">
	<br />
	<strong><bean:message key="label.protocols.noFiles" bundle="PROTOCOLS_RESOURCES"/></strong>
</div>
</logic:empty>

<logic:equal value="true" name="canBeWritten">
<html:link href="<%="/protocols.do?method=manageFilesForProtocol&OID=" + OID %>"><bean:message key="link.protocols.manageFiles" bundle="PROTOCOLS_RESOURCES"/></html:link>
</logic:equal>

</logic:present>

<logic:notPresent name="protocolFiles">
<div align="center">
	<br />
	<strong><bean:message key="label.protocols.noFiles" bundle="PROTOCOLS_RESOURCES"/></strong>
</div>
</logic:notPresent>

</td>
</tr>
</table>

<table width="100%">
<tr>

<td width="50%" valign="top">

<div class="infobox">
	<p class="dinline"><strong><bean:message key="label.protocols.internalResponsibles" bundle="PROTOCOLS_RESOURCES"/></strong></p>
</div>
<logic:present name="internalResponsibles">
<fr:view name="internalResponsibles" schema="show.protocol.responsibles">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight thright"/>
		<fr:property name="columnClasses" value="aleft,aleft,,"/>
	</fr:layout>
</fr:view>
</logic:present>

</td>

<td width="50%" valign="top">

<div class="infobox">
	<p class="dinline"><strong><bean:message key="label.protocols.externalResponsibles" bundle="PROTOCOLS_RESOURCES"/></strong></p>
</div>

<logic:present name="externalResponsibles">
<fr:view name="externalResponsibles" schema="show.protocol.responsibles">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight thright"/>
		<fr:property name="columnClasses" value="aleft,aleft,,"/>
	</fr:layout>
</fr:view>
</logic:present>

</td>


</tr>
</table>


</logic:present>
</p>