/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.elevation.el_station.entity;

import javax.validation.constraints.NotBlank;

import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.elevation.el_line.entity.ELLine;
import com.jeesite.modules.group.entity.Group;
import com.jeesite.modules.line.entity.Line;
import com.jeesite.modules.sys.entity.Employee;
import org.hibernate.validator.constraints.Length;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * 高程——测站Entity
 *
 * @author su
 * @version 2022-05-17
 */
@Table(name = "el_station", alias = "a", columns = {
        @Column(name = "id", attrName = "id", label = "id", isPK = true),
        @Column(name = "line_id", attrName = "lineId", label = "导线id"),
        @Column(name = "station_name", attrName = "stationName", label = "测站名称", queryType = QueryType.LIKE),
        @Column(name = "fore_stn_id", attrName = "foreStnId", label = "前视站点id"),
        @Column(name = "back_stn_id", attrName = "backStnId", label = "后视站点id"),
        @Column(includeEntity = DataEntity.class),
},

        //2022年5月17日 su添加
        joinTable = {
                @JoinTable(type = JoinTable.Type.LEFT_JOIN, entity = Employee.class, attrName = "employee", alias = "u1",
                        on = "u1.emp_code = a.create_by", columns = {
                        @Column(name = "emp_code", label = "用户编码", isPK = true),
                        @Column(name = "office_code", label = "机构编码", isQuery = false),
                        @Column(name = "office_name", label = "机构名称"),
                }),
                @JoinTable(type = JoinTable.Type.LEFT_JOIN, entity = ELLine.class, attrName = "line", alias = "u2",
                        on = "u2.id = a.line_id", columns = {
                        @Column(name = "id", label = "导线id", isPK = true),
                        @Column(name = "group_id", label = "小组id", isQuery = false),
                        @Column(name = "line_name", label = "导线名称", isQuery = false),
                }),
                @JoinTable(type = JoinTable.Type.LEFT_JOIN, entity = Group.class, attrName = "group", alias = "u3",
                        on = "u3.id = u2.group_id", columns = {
                        @Column(name = "id", label = "小组id", isPK = true),
                        @Column(name = "group_name", label = "小组名称", isQuery = false)
                })
        },

        orderBy = "u3.id ASC, u2.id ASC, a.id ASC", extWhereKeys = "dsfOffice"
)
public class ELStation extends DataEntity<ELStation> {

    private static final long serialVersionUID = 1L;
    private String lineId;        // 导线id
    private String stationName;        // 测站名称
    private String foreStnId;       // 前视站点id
    private String backStnId;       // 后视站点id

    //2022年5月17日 su添加
    //小组
    private Group group;
    //导线
    private ELLine line;

    public ELStation() {
        this(null);
    }

    public ELStation(String id) {
        super(id);
    }

    @NotBlank(message = "导线id不能为空")
    @Length(min = 0, max = 64, message = "导线id长度不能超过 64 个字符")
    public String getLineId() {
        return lineId;
    }

    public void setLineId(String lineId) {
        this.lineId = lineId;
    }

    @NotBlank(message = "测站名称不能为空")
    @Length(min = 0, max = 20, message = "测站名称长度不能超过 20 个字符")
    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getForeStnId() {
        return foreStnId;
    }

    public void setForeStnId(String foreStnId) {
        this.foreStnId = foreStnId;
    }

    public String getBackStnId() {
        return backStnId;
    }

    public void setBackStnId(String backStnId) {
        this.backStnId = backStnId;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public ELLine getLine() {
        return line;
    }

    public void setLine(ELLine line) {
        this.line = line;
    }
}