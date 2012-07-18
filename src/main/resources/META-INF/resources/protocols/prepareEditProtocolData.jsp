<%@ page language="java"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml/>

<h2><bean:message key="label.protocols.edit" bundle="PROTOCOLS_RESOURCES"/> - <bean:write name="protocolBean" property="protocolNumber"/></h2>

<fr:form action="/protocols.do?method=prepareEditInternalResponsibles">


<p class="breadcumbs">
	<span><strong><bean:message key="label.protocol.edit.step1" bundle="PROTOCOLS_RESOURCES"/></strong></span> > 
	<span><bean:message key="label.protocol.edit.step2" bundle="PROTOCOLS_RESOURCES"/></span> > 
	<span><bean:message key="label.protocol.edit.step3" bundle="PROTOCOLS_RESOURCES"/></span> >
	<span><bean:message key="label.protocol.edit.step4" bundle="PROTOCOLS_RESOURCES"/></span>
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

<fr:edit id="protocolData" name="protocolBean" schema="create.protocol">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thright thlight mtop05"/>
		<fr:property name="columnClasses" value=",,tdclear tderror1"/>		
	</fr:layout>
	
</fr:edit>

<p>
	<html:submit bundle="PROTOCOLS_RESOURCES" altKey="submit.submit">
		<bean:message key="submit.next" bundle="PROTOCOLS_RESOURCES" />
	</html:submit>
</p>
</fr:form>
