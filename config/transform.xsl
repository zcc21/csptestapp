<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="xml" indent="yes" />
<xsl:variable name="applicationId" select="document('../pom.xml')//*[local-name() ='csp.cloud.app.cmr.applicationid']"/>
<xsl:template match="@*|node()">
  <xsl:copy>
    <xsl:apply-templates select="@*|node()"/>
  </xsl:copy>
</xsl:template>

<xsl:template match="@name[parent::ehcache]">
  <xsl:attribute name="name">
    <xsl:value-of select="$applicationId" />
    <!-- <xsl:value-of select="document('../pom.xml')//*[local-name() = '']" /> -->
  </xsl:attribute>
</xsl:template>

</xsl:stylesheet>
