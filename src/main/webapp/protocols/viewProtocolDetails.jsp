<%@page import="pt.ist.vaadinframework.fragment.FragmentQuery"%>
<%@page import="module.fileManagement.presentationTier.pages.DocumentBrowse"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<br />
<h2><bean:message key="label.protocols.details" bundle="PROTOCOLS_RESOURCES"/>
</h2>

<div align="center" style="font-weight:bold;">
	<html:errors bundle="PROTOCOLS_RESOURCES"/>
	<html:messages id="message" name="success" message="true" bundle="PROTOCOLS_RESOURCES">
		<bean:write name="message" />
	</html:messages>
</div>


<p>
<logic:present name="protocol">

<bean:define id="OID" type="java.lang.Long" name="protocol" property="OID"/>

<logic:equal value="true" name="canBeWritten">
<html:link action="protocols.do?method=prepareEditProtocolData" paramId="OID" paramName="OID"><bean:message key="link.edit" bundle="MYORG_RESOURCES"/></html:link>
</logic:equal>


<table width="100%">
<tr>
<td width="50%" valign="top">

<div class="infobox" align="center">
	<p class="dinline"><strong><bean:message key="label.protocols.data" bundle="PROTOCOLS_RESOURCES"/></strong></p>
</div>
	
<fr:view name="protocol">
	<fr:schema type="module.protocols.domain.Protocol" bundle="PROTOCOLS_RESOURCES">
		<fr:slot name="protocolNumber"	key="label.protocols.number" bundle="PROTOCOLS_RESOURCES"/>
		<fr:slot name="signedDate" key="label.protocols.signedDate" bundle="PROTOCOLS_RESOURCES"/>
		<fr:slot name="currentAndFutureProtocolHistories" key="label.protocols.actualDates" bundle="PROTOCOLS_RESOURCES">
			<fr:property name="eachSchema" value="show.protocolHistories.toList"/>
			<fr:property name="eachLayout" value="flow"/>
		</fr:slot>
		<fr:slot name="scientificAreas" key="label.protocols.scientificAreas" bundle="PROTOCOLS_RESOURCES"/>
	 	<fr:slot name="protocolAction.protocolActionTypes" key="label.protocol.actionTypes" layout="list" bundle="PROTOCOLS_RESOURCES"/>
		<fr:slot name="protocolAction.otherTypes" key="label.protocol.otherActionTypes" bundle="PROTOCOLS_RESOURCES"/>
		<fr:slot name="observations" key="label.protocols.observations" bundle="PROTOCOLS_RESOURCES"/>
		<fr:slot name="visibilityDescription" key="label.protocols.visibility" bundle="PROTOCOLS_RESOURCES" />
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight thright"/>
		<fr:property name="columnClasses" value="aleft,aleft,,"/>
	</fr:layout>
</fr:view>

</td>

<td width="50%" valign="top" align="center"> 

<div class="infobox">
	<p class="dinline"><strong><bean:message key="label.protocols.files" bundle="PROTOCOLS_RESOURCES"/></strong></p>
</div>

<logic:present name="protocolFiles">
<table width="100%">
<logic:iterate id="file" name="protocolFiles">
	<tr>
	<td width="70%" style="padding-left:20px">
	<bean:define id="url" type="java.lang.String" name="file" property="fileURL"/>
	<a href="<%= url %>"><bean:write name="file" property="file.document.lastVersionedFile.filename"/></a>
	<br />
	</td>
	<td>
	<logic:equal value="true" name="canBeWritten">
		<bean:define id="fileOID" type="java.lang.Long" name="file" property="file.OID"/>
		<html:link page="<%= "/protocols.do?method=removeProtocolFile&protocol=" + OID + "&file=" + fileOID %>">
		<bean:message key="label.remove" bundle="MYORG_RESOURCES" />
		</html:link>
	</logic:equal>
	</td>
	</tr>
</logic:iterate>
</table>
<logic:empty name="protocolFiles">
<div align="center">
	<br />
	<strong><bean:message key="label.protocols.noFiles" bundle="PROTOCOLS_RESOURCES"/></strong>
</div>
</logic:empty>

<logic:equal value="true" name="canBeWritten">

<br />
<hr />
<br />

<html:link page="<%= "/protocols.do?method=uploadProtocolFile&OID=" + OID %>">
<bean:message key="link.protocols.uploadFile" bundle="PROTOCOLS_RESOURCES"/>
</html:link>

<br />
<br />

<bean:define id="dirOID" name="protocol" property="protocolDir.OID"/>
<html:link page="<%="/vaadinContext.do?method=forwardToVaadin#" + 
	new pt.ist.vaadinframework.fragment.FragmentQuery(DocumentBrowse.class).getQueryString() + "?contextPath=" + dirOID + "&now=" + (new java.util.Date()).getTime() %>" target="_blank">
<bean:message key="link.protocols.manageFiles" bundle="PROTOCOLS_RESOURCES"/>
</html:link>





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

<div class="infobox" align="center">
	<p class="dinline"><strong><bean:message key="label.protocols.internalResponsibles" bundle="PROTOCOLS_RESOURCES"/></strong></p>
</div>
<logic:present name="internalResponsibles">
<fr:view name="internalResponsibles">
	<fr:schema type="module.protocols.domain.ProtocolResponsible" bundle="PROTOCOLS_RESOURCES">
		<fr:slot name="unit.partyName" key="label.protocols.unit" bundle="PROTOCOLS_RESOURCES" layout="null-as-label">
			<fr:property name="label" value="-"/>
		</fr:slot>
		<fr:slot name="presentationString" key="label.protocols.responsibles" bundle="PROTOCOLS_RESOURCES" layout="null-as-label">
			<fr:property name="label" value="label.protocols.noResponsibles" />
			<fr:property name="key" value="true" />
			<fr:property name="bundle" value="PROTOCOLS_RESOURCES"/>
		</fr:slot>	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2"/>
		<fr:property name="columnClasses" value="aleft,aleft,,"/>
	</fr:layout>
</fr:view>
</logic:present>

</td>

<td width="50%" valign="top">

<div class="infobox" align="center">
	<p class="dinline"><strong><bean:message key="label.protocols.externalResponsibles" bundle="PROTOCOLS_RESOURCES"/></strong></p>
</div>

<logic:present name="externalResponsibles">
<fr:view name="externalResponsibles">
	<fr:schema type="module.protocols.domain.ProtocolResponsible" bundle="PROTOCOLS_RESOURCES">
		<fr:slot name="unit.partyName" key="label.protocols.unit" bundle="PROTOCOLS_RESOURCES" layout="null-as-label">
			<fr:property name="label" value="-"/>
		</fr:slot>
		<fr:slot name="presentationString" key="label.protocols.responsibles" bundle="PROTOCOLS_RESOURCES" layout="null-as-label">
			<fr:property name="label" value="label.protocols.noResponsibles" />
			<fr:property name="key" value="true" />
			<fr:property name="bundle" value="PROTOCOLS_RESOURCES"/>
		</fr:slot>
		<fr:slot name="countryDescription" key="label.protocols.country" bundle="PROTOCOLS_RESOURCES" />
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2"/>
		<fr:property name="columnClasses" value="aleft,aleft,,"/>
	</fr:layout>
</fr:view>
</logic:present>

</td>


</tr>
</table>


</logic:present>
</p>
