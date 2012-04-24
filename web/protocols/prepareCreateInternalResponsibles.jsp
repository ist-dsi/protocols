<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<h2><bean:message key="label.protocols.create" bundle="PROTOCOLS_RESOURCES"/></h2>

<p class="breadcumbs">
	<span><bean:message key="label.protocol.create.step1" bundle="PROTOCOLS_RESOURCES"/></span> > 
	<span class="actual"><bean:message key="label.protocol.create.step2" bundle="PROTOCOLS_RESOURCES"/></span> > 
	<span><bean:message key="label.protocol.create.step3" bundle="PROTOCOLS_RESOURCES"/></span> >
	<span><bean:message key="label.protocol.create.step4" bundle="PROTOCOLS_RESOURCES"/></span>
</p>
<p></p>

<p>
	<span class="error0">
		<html:errors bundle="PROTOCOLS_RESOURCES"/>
		<html:messages id="message" name="errorMessage" message="true" bundle="PROTOCOLS_RESOURCES">
			<bean:write name="message" />
		</html:messages>
	</span>
</p>


<p>

<bean:message key="label.protocols.internalUnits" bundle="PROTOCOLS_RESOURCES"/>

</p>

<p>

<logic:present name="protocolBean" property="internalResponsibles">

<fr:view name="protocolBean" property="internalResponsibles" schema="view.protocolResponsibleBean">
<fr:layout name="tabular">
	<fr:property name="classes" value="tstyle2"/>
</fr:layout>
</fr:view>

</logic:present>

<fr:form action="/protocols.do?method=updateBean">

<bean:message key="label.protocols.select.new.unit" bundle="PROTOCOLS_RESOURCES"/>

<fr:edit id="searchPerson" name="protocolBean">
<fr:schema type="module.protocols.dto.ProtocolCreationBean" bundle="PROTOCOLS_RESOURCES">
	<fr:slot name="newUnit" layout="autoComplete" key="label.person" bundle="ORGANIZATION_RESOURCES">
        <fr:property name="labelField" value="presentationName"/>
		<fr:property name="format" value="${presentationName}"/>
		<fr:property name="minChars" value="2"/>
		<fr:property name="args" value="provider=module.protocols.presentationTier.providers.UnitPerModelAutoCompleteProvider"/>
		<fr:property name="serviceArgs" value="model=internal"/>
		<fr:property name="size" value="60"/>
		<fr:validator name="pt.ist.fenixWebFramework.rendererExtensions.validators.RequiredAutoCompleteSelectionValidator">
			<fr:property name="message" value="label.pleaseSelectOne.unit"/>
			<fr:property name="bundle" value="ORGANIZATION_RESOURCES"/>
			<fr:property name="key" value="true"/>
		</fr:validator>
	</fr:slot>
</fr:schema>	
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2"/>
	</fr:layout>
</fr:edit>

	<html:submit bundle="PROPERTIES_RESOURCES" property="insertInternalUnit">
		<bean:message key="button.insertUnit" bundle="PROTOCOLS_RESOURCES" />
	</html:submit>

</fr:form>

</p>


<fr:form action="/protocols.do?method=prepareCreateExternalResponsibles">

<p>
	<html:cancel bundle="PROTOCOLS_RESOURCES" altKey="submit.back">
		<bean:message key="submit.back" bundle="PROTOCOLS_RESOURCES" />
	</html:cancel>
	<html:submit bundle="PROTOCOLS_RESOURCES" altKey="submit.submit">
		<bean:message key="submit.next" bundle="PROTOCOLS_RESOURCES" />
	</html:submit>
</p>

</fr:form>