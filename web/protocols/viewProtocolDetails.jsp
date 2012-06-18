<%@page import="pt.ist.vaadinframework.fragment.FragmentQuery"%>
<%@page import="module.fileManagement.presentationTier.pages.DocumentHome"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

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
<html:link href="<%="protocols.do?method=prepareEditProtocolData&OID=" + OID %>"><bean:message key="link.edit" bundle="MYORG_RESOURCES"/></html:link>
</logic:equal>


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

<td width="50%" valign="top" align="center"> 

<div class="infobox">
	<p class="dinline"><strong><bean:message key="label.protocols.files" bundle="PROTOCOLS_RESOURCES"/></strong></p>
</div>

<logic:present name="protocolFiles">
<table width="100%">
<logic:iterate id="file" name="protocolFiles">
	<tr>
	<td width="70%">
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
<html:link page="<%="/vaadinContext.do?method=forwardToVaadin#" + new pt.ist.vaadinframework.fragment.FragmentQuery(DocumentHome.class).getQueryString() + "?contextPath=" + dirOID %>">
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

<div class="infobox">
	<p class="dinline"><strong><bean:message key="label.protocols.internalResponsibles" bundle="PROTOCOLS_RESOURCES"/></strong></p>
</div>
<logic:present name="internalResponsibles">
<fr:view name="internalResponsibles">
	<fr:schema type="module.protocols.domain.ProtocolResponsible" bundle="PROTOCOLS_RESOURCES">
		<fr:slot name="unit.partyName" key="label.protocols.unit" bundle="PROTOCOLS_RESOURCES" layout="null-as-label">
			<fr:property name="label" value="-"/>
		</fr:slot>
		<fr:slot name="presentationString" key="label.protocols.responsibles" bundle="PROTOCOLS_RESOURCES" />
	</fr:schema>
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
<fr:view name="externalResponsibles">
	<fr:schema type="module.protocols.domain.ProtocolResponsible" bundle="PROTOCOLS_RESOURCES">
		<fr:slot name="unit.partyName" key="label.protocols.unit" bundle="PROTOCOLS_RESOURCES" layout="null-as-label">
			<fr:property name="label" value="-"/>
		</fr:slot>
		<fr:slot name="presentationString" key="label.protocols.responsibles" bundle="PROTOCOLS_RESOURCES" />
		<fr:slot name="countryDescription" key="label.protocols.country" bundle="PROTOCOLS_RESOURCES" />
	</fr:schema>
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