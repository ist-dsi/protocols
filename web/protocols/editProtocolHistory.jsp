<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<br />
<h2><bean:message key="label.protocols.editDates" bundle="PROTOCOLS_RESOURCES"/></h2>

<p>

<p class="mtop15">
	<span class="error0">
		<html:messages id="message" message="true" bundle="PROTOCOLS_RESOURCES">
		<bean:write name="message" />
		</html:messages>
	</span>
</p>

<logic:present name="protocolDates">

<fr:edit name="protocolDates" schema="edit.protocolHistory" action="/protocols.do?method=editProtocolHistory">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thlight thright thmiddle" />
		<fr:property name="columnClasses" value=",,tderror1 tdclear" />
	</fr:layout>
	<fr:destination name="cancel" path="/protocols.do?method=showAlerts"/>
	<fr:destination name="invalid" path="/protocols.do?method=invalidProtocolHistoryDates"/>
</fr:edit>

</logic:present>
</p>
