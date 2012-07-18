<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h2>
<bean:message key="label.protocolSystem.configure" bundle="PROTOCOLS_RESOURCES" />
</h2>

<logic:present name="configurationBean">

<fr:form action="/protocols.do?method=protocolSystemConfiguration">

<table width="100%">

<tr>
<td colspan="2" width="100%">
<div class="infobox" align="center">
	<p class="dinline"><strong><bean:message key="label.organizationalModel.define" bundle="PROTOCOLS_RESOURCES"/></strong></p>
</div>
</td>
</tr>


<tr>



<td width="50%" valign="top">
<div class="infobox" align="center">
	<p class="dinline"><strong><bean:message key="label.organizationalModel.internal" bundle="PROTOCOLS_RESOURCES"/></strong></p>
</div>

<fr:edit name="configurationBean">
	<fr:schema type="module.protocols.dto.ProtocolSystemConfigurationBean" bundle="PROTOCOLS_RESOURCES">
		<fr:slot name="internalOrganizationalModel" layout="menu-select" key="label.organizationalModel.internal">
			<fr:property name="providerClass" value="module.organization.presentationTier.renderers.providers.OrganizationalModelProvider"/>
			<fr:property name="format" value="${name}" />
		</fr:slot>
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="form listInsideClear" />
		<fr:property name="columnClasses" value="width100px,,tderror" />
	</fr:layout>
</fr:edit>


</td>

<td width="50%" valign="top">
<div class="infobox" align="center">
	<p class="dinline"><strong><bean:message key="label.organizationalModel.external" bundle="PROTOCOLS_RESOURCES"/></strong></p>
</div>

<fr:edit name="configurationBean">
	<fr:schema type="module.protocols.dto.ProtocolSystemConfigurationBean" bundle="PROTOCOLS_RESOURCES">
		<fr:slot name="externalOrganizationalModel" layout="menu-select" key="label.organizationalModel.external">
			<fr:property name="providerClass" value="module.organization.presentationTier.renderers.providers.OrganizationalModelProvider"/>
			<fr:property name="format" value="${name}" />
		</fr:slot>
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="form listInsideClear" />
		<fr:property name="columnClasses" value="width100px,,tderror" />
	</fr:layout>
</fr:edit>



</td>




</tr>
</table>

<table width="100%">

<tr>
<td>

<div class="infobox" align="center">
	<p class="dinline"><strong><bean:message key="label.groups.administrator.define" bundle="PROTOCOLS_RESOURCES"/></strong></p>
</div>

<fr:edit name="configurationBean">
	<fr:schema type="module.protocols.dto.ProtocolSystemConfigurationBean" bundle="PROTOCOLS_RESOURCES">
		<fr:slot name="administrativeGroup" layout="menu-select" key="label.groups.administrator">
			<fr:property name="providerClass" value="module.protocols.presentationTier.providers.PersistentGroupsProvider"/>
			<fr:property name="format" value="${name}" />
		</fr:slot>
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="form listInsideClear" />
		<fr:property name="columnClasses" value="width100px,,tderror" />
	</fr:layout>
</fr:edit>


</td>
</tr>

</table>

<p align="center">
<html:submit bundle="PROTOCOLS_RESOURCES" altKey="submit.submit">
	<bean:message key="submit.submit" bundle="PROTOCOLS_RESOURCES"/>
</html:submit>
</p>

</fr:form>

</logic:present>
