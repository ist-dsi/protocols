<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<br />
<h2><bean:message key="label.protocols.details" bundle="PROTOCOLS_RESOURCES"/></h2>

<p>
<logic:present name="protocol">

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

<table class="tstyle2">
<logic:iterate id="file" name="protocol" property="protocolFiles">
	<tr><td>
	<!-- 
	<logic:equal name="file" property="visibleToUser" value="true">
		<bean:define id="url"><bean:write name="file" property="downloadUrl"/></bean:define>
			<html:link href="<%= url %>" target="_blank"><bean:write name="file" property="filename"/></html:link>
	</logic:equal>
	<logic:notEqual name="file" property="visibleToUser" value="true">
		<bean:write name="file" property="filename"/>
	</logic:notEqual>
	-->
	</td></tr>
</logic:iterate>
</table>

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