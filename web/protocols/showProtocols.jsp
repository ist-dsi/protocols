<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<br />
<h2><bean:message key="label.protocols.show" bundle="PROTOCOLS_RESOURCES"/></h2>

<p>
<logic:present name="protocols">

	<fr:view name="protocols" schema="show.protocol.toList" >
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2"/>
			<fr:property name="columnClasses" value="acenter,aleft,,aleft"/>
			<fr:property name="link(show)" value="/protocols.do?method=viewProtocolDetails" />
			<fr:property name="key(show)" value="link.protocols.viewDetails" />
			<fr:property name="param(show)" value="OID" />
			<fr:property name="bundle(show)" value="PROTOCOLS_RESOURCES" />
		</fr:layout>
	</fr:view>

</logic:present>
</p>