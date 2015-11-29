<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : flightsTransform.xsl
    Created on : 28 November 2015, 22:29
    Author     : martin
    Description:
        Purpose of transformation follows.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version=".0">
    <xsl:output omit-xml-declaration="yes" indent="yes"/>

    
    <xsl:template match="hotels">
        <xsl:copy>
            <getHotelsResponse>
                <xsl:apply-templates select="hotels/hotel">
                    <xsl:sort select="*"/>
                </xsl:apply-templates>
            </getHotelsResponse>
        </xsl:copy>
        
    </xsl:template>
    <xsl:template match="*">
        <xsl:copy>
            <xsl:apply-templates select="*"/>
        </xsl:copy>
    </xsl:template>
    
</xsl:stylesheet>
