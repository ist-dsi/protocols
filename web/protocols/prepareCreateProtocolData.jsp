<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<h2><bean:message key="label.protocols.create" bundle="PROTOCOLS_RESOURCES"/></h2>

<fr:form action="/protocols.do?method=prepareCreateInternalResponsibles">


<p class="breadcumbs">
	<span><strong><bean:message key="label.protocol.create.step1" bundle="PROTOCOLS_RESOURCES"/></strong></span> > 
	<span><bean:message key="label.protocol.create.step2" bundle="PROTOCOLS_RESOURCES"/></span> > 
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