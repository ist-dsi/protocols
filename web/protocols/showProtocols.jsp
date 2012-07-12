<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<style type="text/css">
.width70 {
	width: 70px;
}
</style>

<h2><bean:message key="label.protocols.show" bundle="PROTOCOLS_RESOURCES"/></h2>

<p>
<p>
<div align="center">
<logic:present name="protocols">

	<fr:view name="protocols" schema="show.protocol.toList" >
		<fr:layout name="tabular-sortable">
			<fr:property name="classes" value="tstyle2"/>
			<fr:property name="columnClasses" value="acenter,width70, width70, acenter,,,aleft"/>
			<fr:property name="link(show)" value="/protocols.do?method=viewProtocolDetails" />
			<fr:property name="key(show)" value="link.protocols.viewDetails" />
			<fr:property name="param(show)" value="OID" />
			<fr:property name="bundle(show)" value="PROTOCOLS_RESOURCES" />
			
			
			<fr:property name="sortParameter" value="sortBy"/>
       		<fr:property name="sortUrl" value="/protocols.do?method=showProtocols" />
		    <fr:property name="sortBy" value="<%= request.getParameter("sortBy") == null ? "currentProtocolHistory.beginDate=desc" : request.getParameter("sortBy") %>"/>
			<fr:property name="sortableSlots" value="protocolNumber, presentableProtocolHistory.beginDate, presentableProtocolHistory.endDate" />
			
			
		</fr:layout>
	</fr:view>
	
	<logic:empty name="protocols">
		<em style="text-align:center">
		<bean:message key="label.protocols.noResults"  bundle="PROTOCOLS_RESOURCES"/>
		</em>
	</logic:empty>

</logic:present>

<logic:notPresent name="protocols">
<em style="text-align:center">
<bean:message key="label.protocols.noResults"  bundle="PROTOCOLS_RESOURCES"/>
</em>
</logic:notPresent>

</div>
</p>