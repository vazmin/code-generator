<#noparse><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
</#noparse>
<#assign newLine = "\r\n        ">
<mapper namespace="${TableNameMapperPackage}.${table.upperCamelCaseName}Mapper">

    <sql id="tableName">${table.tableName}</sql>

    <sql id="columns_no_id">
        <#list columnList as column><#if column.lowerCamelCaseName != "id">${column.actualColumnName}<#if column_has_next>, </#if><#if column_index % 5 == 0>${newLine}</#if></#if></#list>
    </sql>

<#noparse>
    <sql id="columns">
        id, <include refid="columns_no_id"/>
    </sql>
</#noparse>

    <sql id="columns_join">
        <#list columnList as column>a.${column.actualColumnName}<#if column_has_next>, </#if><#if (column_index + 1) % 5 == 0>${newLine}</#if></#list>
    </sql>

<#noparse>
    <sql id="likeWhere" databaseId="mysql">
    </sql>
</#noparse>

    <sql id="dynamicWhere">
        <where>
        <#assign index = 0>
        <#list columnList as column>
            <#if column.actualColumnName != "id">
            <if test="${column.lowerCamelCaseName} not in {null, ''}"><#if index &gt; 0>AND </#if>a.${column.actualColumnName} = <#noparse>#{</#noparse>${column.lowerCamelCaseName}<#noparse>}</#noparse></if>
                <#assign index = index + 1>
            </#if>
        </#list>
            <include refid="likeWhere"/>
        </where>
    </sql>

    <select id="get" resultType="${table.upperCamelCaseName}" parameterType="long">
    <#noparse>
        SELECT <include refid="columns_join"/>
        FROM <include refid="tableName"/> a
        WHERE a.id = #{id}
    </#noparse>
    </select>

    <select id="getByDynamicWhere" resultType="${table.upperCamelCaseName}">
    <#noparse>
        SELECT <include refid="columns_join"/>
        FROM <include refid="tableName"/> a
        <include refid="dynamicWhere"/>
    </#noparse>
    </select>

    <#noparse>
    <select id="getCount" resultType="int">
        SELECT COUNT(1)
        FROM <include refid="tableName"/> a
        <include refid="dynamicWhere"/>
    </select>
    </#noparse>

    <select id="getList" resultType="${table.upperCamelCaseName}">
    <#noparse>
        SELECT <include refid="columns_join"/>
        FROM <include refid="tableName"/> a
        <include refid="dynamicWhere"/>
        <if test="fullordering not in {null, ''}">
            ORDER BY ${fullordering}
        </if>
    </#noparse>
    </select>

    <insert id="insert" parameterType="${table.upperCamelCaseName}" useGeneratedKeys="true"
            keyProperty="id" >
        INSERT IGNORE INTO <include refid="tableName"/>
        (<include refid="columns_no_id"/>)
        VALUES
        (
        <#list columnList as column><#if column.actualColumnName != "id"><#noparse>#{</#noparse>${column.lowerCamelCaseName}<#noparse>}</#noparse><#if column_has_next>, </#if><#if column_index % 5 == 0>${newLine}</#if></#if></#list>
        )
    </insert>

    <update id="update" parameterType="${table.upperCamelCaseName}">
        UPDATE <include refid="tableName"/>
        <set>
        <#list columnList as column>
            <#if column.actualColumnName != "id">
            <if test="${column.lowerCamelCaseName} not in {null, ''}">${column.actualColumnName}=<#noparse>#{</#noparse>${column.lowerCamelCaseName}<#noparse>}</#noparse><#if column_has_next>, </#if></if>
            </#if>
        </#list>
        </set>
        <#noparse>
        WHERE id = #{id}
        </#noparse>
    </update>

<#noparse>
    <delete id="delete" parameterType="long">
        DELETE FROM <include refid="tableName"/> WHERE id = #{id}
    </delete>

    <delete id="batchDelete">
        DELETE FROM <include refid="tableName"/>
        WHERE id IN
        <foreach item="item" index="index" collection="list"
                 open="(" separator="," close=")">
            #{item}
        </foreach>
    </delete>
</#noparse>
</mapper>
