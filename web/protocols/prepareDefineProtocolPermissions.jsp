<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<h2><bean:message key="label.protocols.create" bundle="PROTOCOLS_RESOURCES"/></h2>

<p class="breadcumbs">
	<span><bean:message key="label.protocol.create.step1" bundle="PROTOCOLS_RESOURCES"/></span> > 
	<span><bean:message key="label.protocol.create.step2" bundle="PROTOCOLS_RESOURCES"/></span> > 
	<span><bean:message key="label.protocol.create.step3" bundle="PROTOCOLS_RESOURCES"/></span> >
	<span><strong><bean:message key="label.protocol.create.step4" bundle="PROTOCOLS_RESOURCES"/></strong></span>
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


<br />

<div align="center">
	<h3><bean:message key="label.protocol.create.step4" bundle="PROTOCOLS_RESOURCES"/></h3>
</div>



<br />




<p>

<hr />

<fr:form action="/protocols.do?method=createProtocol">

<fr:edit id="protocolBean" name="protocolBean" visible="false" />

<p>
	<html:submit bundle="PROTOCOLS_RESOURCES" altKey="submit.back" property="back">
		<bean:message key="submit.back" bundle="PROTOCOLS_RESOURCES" />
	</html:submit>
	<html:submit bundle="PROTOCOLS_RESOURCES" altKey="submit.submit">
		<bean:message key="label.protocols.create" bundle="PROTOCOLS_RESOURCES" />
	</html:submit>
</p>

</fr:form>