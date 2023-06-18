/*!
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * 项目自定义的公共JavaScript，可覆盖jeesite.js里的方法
 */


/**
 * 根据某一栏位值去合并另一栏位（根据cellName合并otherCellName）
 * @param gridName 表格id
 * @param cellName 某一栏位
 * @param otherCellName 另一栏位
 * @constructor
 */
function MergerRowspanToOther(gridName, cellName, otherCellName) {
    // 获取dataGrid表格
    var dataGrid = $("#" + gridName);
    //得到显示到界面的id集合
    var mya = dataGrid.getDataIDs();
    //当前显示多少条
    var length = mya.length;
    //定义合并行数
    var rowSpanTaxCount;
    for (var i = 0; i < length; i += rowSpanTaxCount) {
        //从上到下获取一条信息
        var before = dataGrid.jqGrid('getRowData', mya[i]);
        rowSpanTaxCount = 1;
        for (j = i + 1; j < length; j++) {
            //和上边的信息对比 如果值一样就合并行数+1 然后设置rowspan 让当前单元格隐藏
            var end = dataGrid.jqGrid('getRowData', mya[j]);
            if ((before[cellName] === end[cellName]) && (before[otherCellName] === end[otherCellName])) {
                rowSpanTaxCount++;
                $("#" + gridName + "").setCell(mya[j], otherCellName, '', { display: 'none' });
            } else {
                break;
            }
        }
        // 进行合并
        // 如果rowSpanTaxCount等于1的话就没必要合并了
        if (rowSpanTaxCount !== 1) {
            $("#" + gridName + "").setCell(mya[i], otherCellName, '', '', {rowspan: rowSpanTaxCount});
        }
    }
}