<!-- Copyright 2009-2015 CIP4 -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	version="1.0" xmlns:xjdf="http://www.CIP4.org/JDFSchema_2_0" xmlns:jdf="http://www.CIP4.org/JDFSchema_1_1"
	xmlns:bambi="www.cip4.org/Bambi">
	<xsl:strip-space elements="*" />
	<!-- device processor -->
	<xsl:template match="xjdf:XJDF">
		<xsl:variable name="context" select="@Context" />
		<html>
			<head>
				<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
				<link rel="stylesheet" type="text/css" href="/legacy/css/styles_pc.css" />
				<link rel="icon" href="favicon.ico" type="image/x-icon" />
				<title>
					JDF Single Node
					<xsl:value-of select="@JobID" />
					/
					<xsl:value-of select="@JobPartID" />
				</title>
			</head>
			<body>
				<xsl:call-template name="topnavigation" />
				<table>
					<tr>
						<td>
							<a>
								<xsl:attribute name="href"><xsl:value-of
									select="$context" />/showQueue/<xsl:value-of
									select="@DeviceID" /></xsl:attribute>
								Back to Queue
							</a>
						</td>
						<td>
							<a>
								<xsl:attribute name="href"><xsl:value-of
									select="$context" />/showDevice/<xsl:value-of
									select="@DeviceID" /></xsl:attribute>
								Back to Device
							</a>
						</td>
						<td>
							<a>
								<xsl:attribute name="href">./<xsl:value-of
									select="@DeviceID" />?qeID=<xsl:value-of select="@QueueEntryID" /></xsl:attribute>
								Back to List of JDF nodes
							</a>
						</td>
						<td>
							<a>
								<xsl:attribute name="href">./<xsl:value-of
									select="@DeviceID" />?raw=true&amp;qeID=<xsl:value-of
									select="@QueueEntryID" />&amp;JobPartID=<xsl:value-of
									select="@JobPartID" /></xsl:attribute>
								Show Raw XJDF
							</a>
						</td>
						<td>
							<a>
								<xsl:attribute name="href">./<xsl:value-of
									select="@DeviceID" />?raw=true&amp;format=JDF&amp;qeID=<xsl:value-of
									select="@QueueEntryID" />&amp;JobPartID=<xsl:value-of
									select="@JobPartID" /></xsl:attribute>
								Show Raw JDF
							</a>
						</td>
					</tr>
				</table>

				<h1>
					JDF Single Node
					<xsl:value-of select="@JobID" />
					/
					<xsl:value-of select="@JobPartID" />
				</h1>
				<xsl:if test="@CommentURL">
					<a>
						<xsl:attribute name="href"><xsl:value-of
							select="@CommentURL" /></xsl:attribute>
						External Job Description
					</a>
				</xsl:if>

				<hr />
				<xsl:call-template name="printAttributelines">
					<xsl:with-param name="x1" select="'Context'" />
					<xsl:with-param name="x2" select="'xsi:type'" />
					<xsl:with-param name="x3" select="'DeviceID'" />
					<xsl:with-param name="x4" select="'CommentURL'" />
					<xsl:with-param name="x5" select="'ID'" />
				</xsl:call-template>
				<hr />
				<table Border="0" cellspacing="0">
					<xsl:call-template name="summarizeSets">
						<xsl:with-param name="usage" select="'Input'" />
					</xsl:call-template>
					<tr />
					<xsl:call-template name="summarizeSets">
						<xsl:with-param name="usage" select="'Output'" />
					</xsl:call-template>
				</table>
				<hr />
				<xsl:apply-templates />
			</body>
		</html>
	</xsl:template>

	<!-- ///////////////////////////////////////////////// -->

	<xsl:template match="xjdf:Comment | jdf:Comment">
		<table>
			<tr>
				<td>
					<em>
						User Comment:
						<xsl:if test="@Name">
							-
							<xsl:value-of select="@Name" />
						</xsl:if>
					</em>
				</td>
				<td border="2">
					<xsl:value-of select="." />
				</td>
			</tr>
		</table>
	</xsl:template>

	<!-- ///////////////////////////////////////////////// -->

	<xsl:template match="xjdf:GeneralID | xjdf:GeneralID">
		<table Border="0" cellspacing="0">
			<tr>
				<td nowrap="true">
					<em>
						<xsl:value-of select="@IDUsage" />
						:
					</em>
				</td>
				<td nowrap="true">
					<xsl:value-of select="@IDValue" />
				</td>
			</tr>
		</table>
	</xsl:template>

	<!-- ///////////////////////////////////////////////// -->
	<xsl:template match="xjdf:ParameterSet">
		<xsl:call-template name="set">
			<xsl:with-param name="header" select="'Parameter Set:'" />
		</xsl:call-template>
	</xsl:template>

	<!-- ///////////////////////////////////////////////// -->

	<xsl:template match="xjdf:Parameter">
		<xsl:call-template name="xjdfResource">
			<xsl:with-param name="header" select="'Parameter'" />
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="xjdf:IntentSet">
		<xsl:call-template name="set">
			<xsl:with-param name="header" select="'Product Intent Set:'" />
		</xsl:call-template>
	</xsl:template>

	<!-- ///////////////////////////////////////////////// -->

	<xsl:template match="xjdf:Intent">
		<xsl:call-template name="set">
			<xsl:with-param name="header" select="'Product Intent Resource'" />
		</xsl:call-template>
	</xsl:template>
	<!-- ///////////////////////////////////////////////// -->

	<xsl:template match="xjdf:ResourceSet">
		<xsl:call-template name="set">
			<xsl:with-param name="header" select="'Resource Set:'" />
		</xsl:call-template>
	</xsl:template>

	<!-- ///////////////////////////////////////////////// -->

	<xsl:template match="xjdf:Resource">
		<xsl:call-template name="xjdfResource">
			<xsl:with-param name="header" select="'Resource'" />
		</xsl:call-template>
	</xsl:template>

	<!-- ///////////////////////////////////////////////// -->

	<xsl:template match="xjdf:PartAmount|jdf:PartAmount ">
		<h4>Amounts:</h4>
		<xsl:call-template name="printAttributelines">
			<xsl:with-param name="prefix" select="''" />
		</xsl:call-template>
		<xsl:apply-templates />
	</xsl:template>

	<!-- ///////////////////////////////////////////////// -->

	<xsl:template match="xjdf:Position|jdf:Position">
		<h4>Fold Sheet Position (Position):</h4>
		<xsl:call-template name="printAttributes">
			<xsl:with-param name="prefix" select="''" />
		</xsl:call-template>
		<xsl:apply-templates />
	</xsl:template>
	<!-- ///////////////////////////////////////////////// -->

	<xsl:template match="xjdf:StripCellParams|jdf:StripCellParams">
		<h4>Page Cell Details (StripCellParams):</h4>
		<xsl:call-template name="printAttributelines">
			<xsl:with-param name="prefix" select="''" />
		</xsl:call-template>
		<xsl:apply-templates />
	</xsl:template>
	<!-- ///////////////////////////////////////////////// -->
	<xsl:template match="xjdf:JMF|jdf:JMF">
		<h3>
			JMF Message Header - Sent at:
			<xsl:value-of select="@TimeStamp" />
			by Sender:
			<xsl:value-of select="@SenderID" />
		</h3>
		<xsl:call-template name="printAttributelines">
			<xsl:with-param name="prefix" select="''" />
			<xsl:with-param name="x1" select="'xsi:type'" />
			<xsl:with-param name="x2" select="'TimeStamp'" />
			<xsl:with-param name="x3" select="'SenderID'" />
		</xsl:call-template>
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template name="message">
		<hr />
		<h4>
			<xsl:if test="@ChannelMode">
				<xsl:value-of select="@ChannelMode" />
			</xsl:if>
			<xsl:text> </xsl:text>
			<xsl:value-of select="name()" />
			- Type:
			<xsl:value-of select="@Type" />
			- ID:
			<xsl:value-of select="@ID" />
			<xsl:if test="@Time">
				Sent at:
				<xsl:value-of select="@Time" />
			</xsl:if>
			<xsl:if test="@SenderID">
				by Sender:
				<xsl:value-of select="@SenderID" />
			</xsl:if>
		</h4>
		<xsl:call-template name="printAttributes">
			<xsl:with-param name="prefix" select="''" />
			<xsl:with-param name="x1" select="'xsi:type'" />
			<xsl:with-param name="x2" select="'Type'" />
			<xsl:with-param name="x3" select="'SenderID'" />
			<xsl:with-param name="x4" select="'Time'" />
			<xsl:with-param name="x5" select="'ChannelMode'" />
		</xsl:call-template>
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="xjdf:Query|jdf:Query">
		<xsl:call-template name="message">
			<xsl:with-param name="prefix" select="''" />
		</xsl:call-template>
	</xsl:template>
	<xsl:template match="xjdf:Signal|jdf:Signal">
		<xsl:call-template name="message">
			<xsl:with-param name="prefix" select="''" />
		</xsl:call-template>
	</xsl:template>
	<xsl:template match="xjdf:Command|jdf:Command">
		<xsl:call-template name="message">
			<xsl:with-param name="prefix" select="''" />
		</xsl:call-template>
	</xsl:template>
	<xsl:template match="xjdf:Response|jdf:Response">
		<xsl:call-template name="message">
			<xsl:with-param name="prefix" select="''" />
		</xsl:call-template>
	</xsl:template>
	<xsl:template match="xjdf:Registration|jdf:Registration">
		<xsl:call-template name="message">
			<xsl:with-param name="prefix" select="''" />
		</xsl:call-template>
	</xsl:template>


	<!-- ///////////////////////////////////////////////// -->
	<xsl:template match="xjdf:Subscription|jdf:Subscription">
		<h3>
			Persistent Channel Subscription Target=
			<xsl:value-of select="@URL" />
		</h3>
		<xsl:call-template name="printAttributes">
			<xsl:with-param name="prefix" select="''" />
			<xsl:with-param name="x1" select="'URL'" />
		</xsl:call-template>
		<xsl:apply-templates />
	</xsl:template>

	<!-- ///////////////////////////////////////////////// -->
	<xsl:template match="xjdf:Part|jdf:Part">
		<xsl:call-template name="printAttributes">
			<xsl:with-param name="prefix" select="'Partition:'" />
		</xsl:call-template>
	</xsl:template>

	<!-- ///////////////////////////////////////////////// -->

	<xsl:template match="xjdf:MISDetails|jdf:MISDetails">
		<xsl:call-template name="printAttributes">
			<xsl:with-param name="prefix" select="'Cost Charging:'" />
		</xsl:call-template>
		<xsl:apply-templates />
	</xsl:template>
	<!-- ///////////////////////////////////////////////// -->

	<xsl:template match="xjdf:FileSpec|jdf:FileSpec">
		<table Border="0" cellspacing="0">
			<tr>

				<td nowrap="true">
					<em>
						File:
					</em>
				</td>
				<td nowrap="true">
					<a>
						<xsl:attribute name="href"><xsl:value-of
							select="@URL" /></xsl:attribute>
						<xsl:value-of select="@URL" />
					</a>
				</td>
				<td>
					<xsl:call-template name="printAttributes">
						<xsl:with-param name="x1" select="'URL'" />
					</xsl:call-template>
				</td>
			</tr>
		</table>
		<xsl:apply-templates />
	</xsl:template>

	<!-- ///////////////////////////////////////////////// -->

	<xsl:template match="xjdf:SeparationSpec|jdf:SeparationSpec">
		<td>
			<xsl:value-of select="@Name" />
			,
		</td>
	</xsl:template>
	<!-- ///////////////////////////////////////////////// -->

	<xsl:template match="xjdf:Dependent">
		<table Border="0" cellspacing="0">
			<tr>
				<td nowrap="true">
					<a>
						<xsl:attribute name="href">./<xsl:value-of
							select="/*/@DeviceID" />?qeID=<xsl:value-of select="/*/@QueueEntryID" />&amp;JobPartID=<xsl:value-of
							select="@JobPartID" /></xsl:attribute>
						Dependent
						<xsl:if test="../@Usage='Input'">
							prior
						</xsl:if>
						<xsl:if test="../@Usage='Output'">
							next
						</xsl:if>
						Workstep:
					</a>
				</td>

				<xsl:for-each select="@*">
					<td nowrap="true">
						<xsl:value-of select="name()" />
					</td>
					<td>
						=
						<xsl:value-of select="." />
					</td>
					<td />

				</xsl:for-each>
			</tr>
		</table>

	</xsl:template>

	<!-- ///////////////////////////////////////////////// -->
	<xsl:template match="xjdf:ColorantOrder|jdf:ColorantOrder">
		<xsl:call-template name="separationList" />
	</xsl:template>
	<!-- ///////////////////////////////////////////////// -->
	<xsl:template match="xjdf:ColorsUsed|jdf:ColorsUsed">
		<xsl:call-template name="separationList" />
	</xsl:template>

	<!-- ///////////////////////////////////////////////// -->
	<xsl:template match="xjdf:ProductList">
		<h3>List Of Products</h3>
		<xsl:apply-templates />
	</xsl:template>
	<!-- ///////////////////////////////////////////////// -->
	<xsl:template match="xjdf:AuditPool|jdf:AuditPool">
		<h2>AuditPool</h2>
		<xsl:apply-templates />
		<hr />
	</xsl:template>
	<!-- ///////////////////////////////////////////////// -->

	<xsl:template match="xjdf:Created|jdf:Created">
		<xsl:call-template name="audit" />
	</xsl:template>
	<xsl:template match="xjdf:Modified|jdf:Modified">
		<xsl:call-template name="audit" />
	</xsl:template>
	<xsl:template match="xjdf:Deleted|jdf:Deleted">
		<xsl:call-template name="audit" />
	</xsl:template>
	<xsl:template match="xjdf:PhaseTime|jdf:PhaseTime">
		<xsl:call-template name="audit" />
	</xsl:template>
	<xsl:template match="xjdf:ResourceAudit|jdf:ResourceAudit">
		<xsl:call-template name="audit" />
	</xsl:template>
	<xsl:template match="xjdf:ProcessRun|jdf:ProcessRun">
		<xsl:call-template name="audit" />
	</xsl:template>

	<!-- ///////////////////////////////////////////////// -->
	<xsl:template name="audit">
		<xsl:param name="header" />
		<xsl:param name="xx1" />
		<h3>
			<xsl:value-of select="name()" />
			Audit:
			<xsl:value-of select="@TimeStamp" />
			<xsl:if test="$header">
				-
				<xsl:value-of select="$header" />
			</xsl:if>
		</h3>
		<xsl:call-template name="printAttributelines">
			<xsl:with-param name="x1" select="'TimeStamp'" />
			<xsl:with-param name="x2" select="'AgentName'" />
			<xsl:with-param name="x3" select="'AgentVersion'" />
			<xsl:with-param name="x4" select="'ID'" />
			<xsl:with-param name="x5" select="'Author'" />
			<xsl:with-param name="x6" select="'ref'" />
			<xsl:with-param name="x7" select="$xx1" />
		</xsl:call-template>
		<xsl:apply-templates />
	</xsl:template>
	<xsl:template match="xjdf:Notification|jdf:Notification">
		<h3>
			Notification Type=
			<xsl:value-of select="@Type" />
			Class=
			<xsl:value-of select="@Class" />
			Job ID=
			<xsl:value-of select="@JobID" />
		</h3>
		<xsl:call-template name="printAttributes">
			<xsl:with-param name="prefix" select="''" />
			<xsl:with-param name="x1" select="'Type'" />
			<xsl:with-param name="x2" select="'Class'" />
			<xsl:with-param name="x3" select="'TimeStamp'" />
			<xsl:with-param name="x4" select="'JobID'" />
		</xsl:call-template>
		<xsl:apply-templates />
	</xsl:template>

	<!-- ///////////////////////////////////////////////// -->
	<xsl:template match="xjdf:Product">
		<h4>
			<a>
				<xsl:attribute name="name"><xsl:value-of select="@ID" /></xsl:attribute>
			</a>
			Product:
			<xsl:if test="@DescriptiveName">
				(
				<xsl:value-of select="@DescriptiveName" />
				)
			</xsl:if>
		</h4>
		<xsl:call-template name="printAttributelines">
			<xsl:with-param name="x1" select="'DescriptiveName'" />
		</xsl:call-template>
		<xsl:apply-templates />
	</xsl:template>

	<!-- ///////////////////////////////////////////////// -->
	<xsl:template match="xjdf:ColorPool|jdf:ColorPool">
		<h3>Colors in the Job</h3>
		<xsl:apply-templates />
		<hr />
	</xsl:template>
	<!-- ///////////////////////////////////////////////// -->
	<xsl:template match="xjdf:Color|jdf:Color">
		<hr />
		<table>
			<tr valign="bottom">
				<td width="400">
					<h4 valign="bottom">
						Color:
						<xsl:value-of select="@Name" />
					</h4>
				</td>
				<td width="50">
					<xsl:attribute name="bgcolor"><xsl:value-of
						select="@HTMLColor" /></xsl:attribute>
				</td>
			</tr>
		</table>
		<xsl:call-template name="printAttributelines">
			<xsl:with-param name="x1" select="'Name'" />
			<xsl:with-param name="x2" select="'HTMLColor'" />
		</xsl:call-template>
	</xsl:template>

	<!-- ///////////////////////////////////////////////// -->
	<xsl:template match="xjdf:MarkObject|jdf:MarkObject">
		<xsl:call-template name="placedobject">
			<xsl:with-param name="header" select="'Printer Mark'" />
		</xsl:call-template>
	</xsl:template>
	<!-- ///////////////////////////////////////////////// -->
	<xsl:template match="xjdf:ContentObject|jdf:ContentObject">
		<xsl:call-template name="placedobject">
			<xsl:with-param name="header" select="'Imposed Page'" />
		</xsl:call-template>
	</xsl:template>
	<!-- ///////////////////////////////////////////////// -->
	<xsl:template name="placedobject">
		<xsl:param name="header" />
		<h4>
			<xsl:value-of select="$header" />
			<xsl:if test="@Ord">
				<xsl:text />
				<xsl:value-of select="@Ord" />
			</xsl:if>
		</h4>
		<xsl:call-template name="printAttributelines">
			<xsl:with-param name="x1" select="'Ord'" />
		</xsl:call-template>
		<xsl:apply-templates />
	</xsl:template>
	<!-- ///////////////////////////////////////////////// -->

	<xsl:template match="xjdf:Media|jdf:Media">
		<h4>
			Media -
			<xsl:value-of select="@MediaType" />
		</h4>
		<xsl:call-template name="printAttributelines">
			<xsl:with-param name="x1" select="'MediaType'" />
		</xsl:call-template>
		<xsl:apply-templates />
	</xsl:template>

	<!-- ///////////////////////////////////////////////// -->
	<xsl:template match="xjdf:Queue|jdf:Queue">
		<h4>
			Queue Status:
			<xsl:value-of select="@Status" />
			<xsl:value-of select="@Size" />
		</h4>
		<xsl:call-template name="printAttributelines">
			<xsl:with-param name="x1" select="'Status'" />
			<xsl:with-param name="x2" select="'Size'" />
		</xsl:call-template>
		<xsl:apply-templates />
	</xsl:template>
	<xsl:template match="xjdf:QueueEntry|jdf:QueueEntry">
		<h4>
			QueueEntry
			<xsl:value-of select="@QueueEntryID" />
			Status:
			<xsl:value-of select="@Status" />
		</h4>
		<xsl:call-template name="printAttributelines">
			<xsl:with-param name="x1" select="'Status'" />
			<xsl:with-param name="x2" select="'QueueEntryID'" />
		</xsl:call-template>
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="xjdf:QueueFilter|jdf:QueueFilter">
		<xsl:call-template name="short">
			<xsl:with-param name="printme" select="''" />
		</xsl:call-template>
	</xsl:template>
	<!-- ///////////////////////////////////////////////// -->

	<xsl:template match="xjdf:DeviceColorantOrder|jdf:DeviceColorantOrder">
		<xsl:call-template name="separationList" />
	</xsl:template>

	<!-- ///////////////////////////////////////////////// -->
	<xsl:template match="xjdf:ColorantParams|jdf:ColorantParams">
		<xsl:call-template name="separationList" />
	</xsl:template>

	<!-- ///////////////////////////////////////////////// -->
	<xsl:template match="xjdf:ColorantAlias|jdf:ColorantAlias">
		<table Border="0" cellspacing="0">
			<tr>
				<td>
					ColorantAlias - ReplacementColorantName =
					<xsl:value-of select="@ReplacementColorantName" />
					:
				</td>
				<xsl:apply-templates />
			</tr>
		</table>

	</xsl:template>
	<!-- ///////////////////////////////////////////////// -->

	<xsl:template match="xjdf:ComChannel|jdf:ComChannel">
		<xsl:variable name="prefix">
			<xsl:value-of select="@ChannelType" />
			=
			<xsl:value-of select="@Locator" />
		</xsl:variable>
		<xsl:call-template name="printAttributes">
			<xsl:with-param name="prefix" select="$prefix" />
			<xsl:with-param name="x1" select="'ChannelType'" />
			<xsl:with-param name="x2" select="'Locator'" />
		</xsl:call-template>
	</xsl:template>
	<!-- ///////////////////////////////////////////////// -->
	<xsl:template match="xjdf:Milestone|jdf:Milestone">
		<h4>
			Milestone:
			<xsl:value-of select="@MilestoneType" />
			(
			<xsl:value-of select="@DescriptiveName" />
			)
		</h4>
		<xsl:call-template name="printAttributes">
			<xsl:with-param name="prefix" select="''" />
			<xsl:with-param name="x1" select="'MilestoneType'" />
			<xsl:with-param name="x2" select="'DescriptiveName'" />
		</xsl:call-template>
		<xsl:apply-templates />
	</xsl:template>
	<!-- ///////////////////////////////////////////////// -->

	<xsl:template match="xjdf:Device|jdf:Device">
		<h3>
			Device:
			<xsl:value-of select="@DeviceID" />
			(
			<xsl:value-of select="@DescriptiveName" />
			)
		</h3>
		<xsl:call-template name="printAttributelines">
			<xsl:with-param name="prefix" select="''" />
			<xsl:with-param name="x1" select="'DeviceID'" />
			<xsl:with-param name="x2" select="'Class'" />
			<xsl:with-param name="x3" select="'DescriptiveName'" />
		</xsl:call-template>
		<xsl:apply-templates />
	</xsl:template>
	<!-- ///////////////////////////////////////////////// -->
	<xsl:template match="xjdf:NodeInfo|jdf:NodeInfo">
		<h3>
			<font>
				<xsl:if test="@NodeStatus='Running'">
					<xsl:attribute name="color">#00ff00</xsl:attribute>
				</xsl:if>
				<xsl:if test="@NodeStatus='Waiting'">
					<xsl:attribute name="color">#0000ff</xsl:attribute>
				</xsl:if>
				<xsl:if test="@NodeStatus='Spawned'">
					<xsl:attribute name="color">#00ff00</xsl:attribute>
				</xsl:if>
				<xsl:if test="@NodeStatus='Aborted'">
					<xsl:attribute name="color">#ff0000</xsl:attribute>
				</xsl:if>
				<xsl:if test="@NodeStatus='Completed'">
					<xsl:attribute name="color">#66aa66</xsl:attribute>
				</xsl:if>
				Job Step Information - Status:
				<xsl:if test="@NodeStatus">
					<xsl:text />
					<xsl:value-of select="@NodeStatus" />
				</xsl:if>
				<xsl:if test="@NodeStatusDetails">
					<xsl:text> / </xsl:text>
					<xsl:value-of select="@NodeStatusDetails" />
				</xsl:if>
			</font>
		</h3>
		<xsl:call-template name="printAttributelines">
			<xsl:with-param name="x1" select="'NodeStatus'" />
			<xsl:with-param name="x2" select="'NodeStatusDetails'" />
		</xsl:call-template>
		<xsl:apply-templates />
	</xsl:template>
	<!-- ///////////////////////////////////////////////// -->

	<xsl:template match="xjdf:Address|jdf:Address">
		<h4>Address:</h4>
		<xsl:if test="@Street">
			<xsl:value-of select="@Street" />
			<br />
		</xsl:if>
		<xsl:if test="@PostBox">
			<xsl:value-of select="@PostBox" />
			<br />
		</xsl:if>
		<xsl:if test="ExtendedAddress">
			<xsl:value-of select="ExtendedAddress" />
			<br />
		</xsl:if>
		<xsl:if test="@CountryCode">
			<xsl:value-of select="@CountryCode" />
			<xsl:text />
		</xsl:if>
		<xsl:if test="@PostalCode">
			<xsl:value-of select="@PostalCode" />
			<xsl:text />
		</xsl:if>
		<xsl:if test="@City">
			<xsl:value-of select="@City" />
			<br />
		</xsl:if>
		<xsl:if test="@Region">
			<xsl:value-of select="@Region" />
			<xsl:text />
		</xsl:if>
		<xsl:if test="@Country">
			<xsl:value-of select="@Country" />
			<br />
		</xsl:if>
		<hr />
	</xsl:template>

	<!-- ........................................ -->
	<xsl:template match="xjdf:CutBlock|jdf:CutBlock">
		<h4>
			Cutblock:
			<xsl:value-of select="@BlockName" />
		</h4>
		<xsl:call-template name="printAttributelines">
			<xsl:with-param name="printme" select="''" />
			<xsl:with-param name="x1" select="'BlockName'" />
		</xsl:call-template>
		<xsl:apply-templates />

	</xsl:template>
	<!-- ........................................ -->

	<xsl:template match="xjdf:Person|jdf:Person">
		<xsl:if test="@NamePrefix">
			<xsl:value-of select="@NamePrefix" />
			<xsl:text />
		</xsl:if>
		<xsl:if test="@FirstName">
			<xsl:value-of select="@FirstName" />
			<xsl:text />
		</xsl:if>
		<xsl:if test="@FamilyName">
			<xsl:value-of select="@FamilyName" />
			<xsl:text />
		</xsl:if>
		<xsl:if test="@NameSuffix">
			<xsl:value-of select="@NameSuffix" />
			<xsl:text />
		</xsl:if>
		<xsl:call-template name="printAttributes">
			<xsl:with-param name="printme" select="''" />
			<xsl:with-param name="x1" select="'NamePrefix'" />
			<xsl:with-param name="x2" select="'FirstName'" />
			<xsl:with-param name="x3" select="'FamilyName'" />
			<xsl:with-param name="x4" select="'NameSuffix'" />
		</xsl:call-template>
		<xsl:apply-templates />

	</xsl:template>
	<!-- ///////////////////////////////////////////////// -->

	<xsl:template match="jdf:NumberSpan">
		<xsl:call-template name="span" />
	</xsl:template>
	<xsl:template match="jdf:OptionSpan">
		<xsl:call-template name="span" />
	</xsl:template>
	<xsl:template match="jdf:DurationSpan">
		<xsl:call-template name="span" />
	</xsl:template>
	<xsl:template match="jdf:NameSpan">
		<xsl:call-template name="span" />
	</xsl:template>
	<xsl:template match="jdf:ShapeSpan">
		<xsl:call-template name="span" />
	</xsl:template>
	<xsl:template match="jdf:XYPairSpan">
		<xsl:call-template name="span" />
	</xsl:template>
	<xsl:template match="jdf:MatrixSpan">
		<xsl:call-template name="span" />
	</xsl:template>
	<xsl:template match="jdf:StringSpan">
		<xsl:call-template name="span" />
	</xsl:template>
	<xsl:template match="jdf:TimeSpan">
		<xsl:call-template name="span" />
	</xsl:template>
	<xsl:template match="jdf:EnumerationSpan">
		<xsl:call-template name="span" />
	</xsl:template>
	<xsl:template match="jdf:IntegerSpan">
		<xsl:call-template name="span" />
	</xsl:template>

	<!-- ///////////////////////////////////////////////// -->
	<!-- nop! -->
	<xsl:template match="xjdf:SpawnInfo" />

	<!-- ///////////////////////////////////////////////// -->

	<xsl:template name="span">
		<xsl:param name="header" />
		<xsl:variable name="prefix">
			<xsl:value-of select="$header" />
			<xsl:text />
			<xsl:value-of select="@Name" />
			:
		</xsl:variable>

		<xsl:call-template name="printAttributes">
			<xsl:with-param name="prefix" select="$prefix" />
			<xsl:with-param name="x1" select="'Name'" />
		</xsl:call-template>
		<xsl:apply-templates>
			<xsl:with-param name="printme" select="''" />
		</xsl:apply-templates>

	</xsl:template>

	<!-- ///////////////////////////////////////////////// -->

	<xsl:template name="separationList">
		<xsl:param name="pre" select="''" />
		<table Border="0" cellspacing="0">
			<tr>
				<td>
					<xsl:if test="$pre">
						<xsl:value-of select="$pre" />
						/
					</xsl:if>
					<xsl:value-of select="name()" />
					:
				</td>
				<xsl:apply-templates />
			</tr>
		</table>
	</xsl:template>

	<!-- ///////////////////////////////////////////////// -->

	<!-- ///////////////////////////////////////////////// -->
	<xsl:template match="xjdf:ScreenSelector|jdf:ScreenSelector">
		<xsl:call-template name="short">
			<xsl:with-param name="printme" select="''" />
		</xsl:call-template>
	</xsl:template>
	<xsl:template match="xjdf:ObservationTarget|jdf:ObservationTarget">
		<xsl:call-template name="short">
			<xsl:with-param name="printme" select="''" />
		</xsl:call-template>
	</xsl:template>
	<xsl:template match="xjdf:ColorSpaceConversionOp|jdf:ColorSpaceConversionOp">
		<xsl:call-template name="short">
			<xsl:with-param name="printme" select="''" />
		</xsl:call-template>
	</xsl:template>
	<xsl:template match="jdf:DeviceInfo">
		<h4>
			DeviceInfo:
			<xsl:value-of select="@DeviceID" />
			Status=
			<xsl:value-of select="@DeviceStatus" />
		</h4>
		<xsl:call-template name="printAttributes">
			<xsl:with-param name="prefix" select="''" />
			<xsl:with-param name="x1" select="'DeviceID'" />
			<xsl:with-param name="x2" select="'DeviceStatus'" />
		</xsl:call-template>
		<xsl:apply-templates />
	</xsl:template>
	<xsl:template match="xjdf:ResourceInfo|jdf:ResourceInfo">
		<h3>
			ResourceInfo:
			<xsl:value-of select="@ResourceName" />
		</h3>
		<xsl:call-template name="printAttributes">
			<xsl:with-param name="prefix" select="''" />
			<xsl:with-param name="x1" select="'ResourceName'" />
		</xsl:call-template>
		<xsl:apply-templates />
	</xsl:template>
	<xsl:template match="xjdf:ResourceQuParams|jdf:ResourceQuParams">
		<xsl:call-template name="short">
			<xsl:with-param name="printme" select="''" />
		</xsl:call-template>
	</xsl:template>
	<xsl:template match="xjdf:StatusQuParams|jdf:StatusQuParams">
		<xsl:call-template name="short">
			<xsl:with-param name="printme" select="''" />
		</xsl:call-template>
	</xsl:template>
	<xsl:template match="xjdf:JobPhase|jdf:JobPhase">
		<xsl:call-template name="status">
			<xsl:with-param name="printme" select="''" />
		</xsl:call-template>
	</xsl:template>
	<!-- ///////////////////////////////////////////////// -->
	<xsl:template match="xjdf:ObjectResolution">
		<xsl:call-template name="short">
			<xsl:with-param name="printme" select="''" />
		</xsl:call-template>
	</xsl:template>
	<!-- ///////////////////////////////////////////////// -->
	<xsl:template match="xjdf:ImageCompression|jdf:ImageCompression">
		<xsl:call-template name="short">
			<xsl:with-param name="printme" select="''" />
		</xsl:call-template>
	</xsl:template>
	<!-- ///////////////////////////////////////////////// -->
	<xsl:template match="xjdf:ThinPDFParams">
		<xsl:call-template name="short">
			<xsl:with-param name="printme" select="''" />
		</xsl:call-template>
	</xsl:template>
	<!-- ///////////////////////////////////////////////// -->
	<xsl:template match="xjdf:AdvancedParams|jdf:AdvancedParams">
		<xsl:call-template name="short">
			<xsl:with-param name="printme" select="''" />
		</xsl:call-template>
	</xsl:template>
	<!-- ///////////////////////////////////////////////// -->


	<xsl:template match="xjdf:*">
		<xsl:param name="pre" />
		<xsl:param name="printme" select="'y'" />

		<xsl:call-template name="default">
			<xsl:with-param name="pre" select="$pre" />
			<xsl:with-param name="printme" select="$printme" />
		</xsl:call-template>
	</xsl:template>

	<!-- ///////////////////////////////////////////////// -->
	<xsl:template match="*">
		<xsl:param name="pre" />
		<xsl:param name="printme" select="'y'" />

		<xsl:call-template name="default">
			<xsl:with-param name="pre" select="$pre" />
			<xsl:with-param name="printme" select="$printme" />
		</xsl:call-template>
	</xsl:template>

	<!-- ///////////////////////////////////////////////// -->

	<xsl:template name="short">
		<h4>
			<xsl:value-of select="name()" />
		</h4>
		<xsl:call-template name="defaultshort">
			<xsl:with-param name="printme" select="''" />
		</xsl:call-template>
	</xsl:template>

	<xsl:template name="status">
		<h4>
			<xsl:value-of select="name()" />
			:
			<xsl:value-of select="@Status" />
		</h4>
		<xsl:call-template name="default">
			<xsl:with-param name="printme" select="''" />
			<xsl:with-param name="x1" select="'Status'" />
		</xsl:call-template>
	</xsl:template>
	<!-- ///////////////////////////////////////////////// -->

	<xsl:template name="set">
		<xsl:param name="header" />
		<h2>
			<a>
				<xsl:attribute name="name"><xsl:value-of select="@ID" /></xsl:attribute>
			</a>
			<xsl:if test="@Usage">
				<xsl:value-of select="@Usage" />
				<xsl:text />
			</xsl:if>

			<xsl:value-of select="$header" />
			<xsl:text />
			<xsl:value-of select="@Name" />
			<xsl:if test="@ProcessUsage">
				(
				<xsl:value-of select="@ProcessUsage" />
				)
			</xsl:if>
			<xsl:variable name="cnt"
				select="count(xjdf:Parameter) + count(xjdf:Resource)+ count(xjdf:Intent)" />
			<xsl:if test="$cnt &gt; 1">
				- Parts:
				<xsl:value-of select="$cnt" />
			</xsl:if>
		</h2>

		<xsl:call-template name="printAttributelines">
			<xsl:with-param name="x1" select="'ProcessUsage'" />
			<xsl:with-param name="x2" select="'Name'" />
			<xsl:with-param name="x3" select="'Usage'" />
		</xsl:call-template>
		<xsl:apply-templates />
		<hr />
	</xsl:template>

	<!-- ///////////////////////////////////////////////// -->

	<xsl:template name="xjdfResource">
		<xsl:param name="header" />
		<h3>
			<a>
				<xsl:attribute name="name"><xsl:value-of select="@ID" /></xsl:attribute>
			</a>
			<xsl:value-of select="../@Name" />
			<xsl:if test="@Status">
				Status=
				<xsl:value-of select="@Status" />
			</xsl:if>
			<xsl:if test="@DescriptiveName">
				<xsl:text />
				(
				<xsl:value-of select="@DescriptiveName" />
				)
			</xsl:if>
		</h3>
		<xsl:call-template name="printAttributelines">
			<xsl:with-param name="x1" select="'Status'" />
			<xsl:with-param name="x2" select="'DescriptiveName'" />
		</xsl:call-template>
		<xsl:apply-templates>
			<xsl:with-param name="printme" select="''" />
		</xsl:apply-templates>
		<hr />
	</xsl:template>

	<!-- ///////////////////////////////////////////////// -->
	<xsl:template name="summarizeLine">
		<xsl:param name="usage" />
		<xsl:if test="not($usage) or $usage = @Usage">
			<tr>
				<td>
					<a>
						<xsl:attribute name="href">#<xsl:value-of
							select="@ID" /></xsl:attribute>
						<xsl:value-of select="@Name" />
					</a>
				</td>
				<td>
					<xsl:value-of select="@Usage" />
				</td>
				<td>
					<a>
						<xsl:attribute name="href">#<xsl:value-of
							select="@ID" /></xsl:attribute>
						<xsl:value-of select="@ID" />
					</a>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!-- ///////////////////////////////////////////////// -->
	<xsl:template name="summarizeSets">
		<xsl:param name="usage" select="''" />
		<xsl:param name="x1" select="''" />
		<xsl:param name="x2" select="''" />
		<xsl:param name="x3" select="''" />
		<xsl:param name="x4" select="''" />
		<xsl:param name="x5" select="''" />
		<xsl:param name="x6" select="''" />
		<xsl:param name="x7" select="''" />
		<xsl:param name="x8" select="''" />
		<th>
			List of Root
			<xsl:value-of select="$usage" />
			Resources
		</th>
		<tr>
			<td>
				Resource Type
			</td>
			<td>
				Input / Output
			</td>
			<td>
				Resource ID
			</td>
		</tr>

		<xsl:for-each select="xjdf:IntentSet">
			<xsl:call-template name="summarizeLine">
				<xsl:with-param name="usage" select="$usage" />
			</xsl:call-template>
		</xsl:for-each>
		<xsl:for-each select="xjdf:ResourceSet">
			<xsl:call-template name="summarizeLine">
				<xsl:with-param name="usage" select="$usage" />
			</xsl:call-template>
		</xsl:for-each>
		<xsl:for-each select="xjdf:ParameterSet">
			<xsl:call-template name="summarizeLine">
				<xsl:with-param name="usage" select="$usage" />
			</xsl:call-template>
		</xsl:for-each>
	</xsl:template>

	<xsl:include href="topnavigation.xsl" />
	<xsl:include href="StandardXML.xsl" />
	
</xsl:stylesheet>