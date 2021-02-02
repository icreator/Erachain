function assets(data) {
    var output = '';
    var notDisplayPages = data.notDisplayPages;
    var numberShiftDelta = data.pageSize;
    output += lastBlock(data.lastBlock);
    var start = data.start;
    if (!notDisplayPages) {
        //output += pagesComponentBeauty(start, data.Label_Assets, data.lastNumber, data.pageSize, 'start');
        output += pagesComponent2(data);
    }


    output += '<table width="1280" border=0><tr><td align=left><br>';
    output += '<a href="?assets"' + get_lang() + '><h3 style="display:inline;">' + data.Label_Title + '</h3></a>';
    output += '<br><br>';

    output += '<table width=80% BORDER=0 cellpadding=10 cellspacing=0 ' +
        'class="tiny table table-striped" style="border: 1px solid #ddd;"><tr>';
    output += '<td><b>' + data.Label_table_asset_key + ': <b>' + data.Label_table_asset_name +
        '<td><b>' + data.Label_table_asset_type + '<td><b>' + data.Label_table_asset_owner;
    output += '<td><b>' + data.Label_table_asset_orders + '<td><b>' + data.Label_table_asset_amount
         + '<td><b>' + data.Label_table_asset_scale;

    //Отображение таблицы элементов активов
    //var length = Object.keys(data.pageItems).length;
    //for (var i = 0; i < length - 1; i++) {
    for (var i in data.pageItems) {
        var item = data.pageItems[i];
        output += '<tr>';
        output += '<td> <a href=?asset=' + item.key + get_lang() + '>';
        output += '<b>' + item.key + '</b>: ';
        if (item.icon.length > 0)
            output += '<img src="data:image/gif;base64,' + item.icon + '"  style="width:2em;" /> ';

        output += escapeHtml(item.name);
        output += '</a>';
        output += '<td>' + item.assetTypeNameFull;
        ////output += '<td>' + escapeHtml(item.description.substr(0, 60));

        output += '<td><a href=?address=' + item.owner + get_lang() + '>';
        if (item.hasOwnProperty('person'))
            output += '[' + item.person_key + ']' + escapeHtml(item.person);
        else
            output += item.owner;
        output += '</a></td>';

        output += '<td>' + item.orders;
        output += '<td>' + item.quantity;
        output += '<td>' + item.scale;

    }
    if (!notDisplayPages) {
        output += '</table></td></tr></table>';
        output += pagesComponent2(data);
    }
    return output;
}

function asset(data, forPrint) {

    var output = '';

    if (!forPrint)
        output += lastBlock(data.lastBlock);

    if (!data.item) {
        output += '<h2>Not found</h2>';
        return output;
    }

    if (data.hasOwnProperty('error')) {
        output += '<br><h5>' + data.error + '</h5>';

        return output;
    }

    output += '<table id=blocks BORDER=0 cellpadding=15 cellspacing=0 width="1300">';
    output += '<tr><td align=left>';
    output += '<table><tr style="vertical-align:top">';

    var item = data.item;
    ////// HEAD
    output += itemHead(item, forPrint);

    //////// BODY
    output += '<p style="font-size:1.3em">';

    if (item.isUnique) {
        output += '<b>' + item.Label_Unique + '</b>';
    } else {
        if (item.isUnlimited) {
            output += '<b>' + item.Label_Unlimited + '</b>';
        } else {
            output += item.Label_Quantity + ': <b>' + addCommas(item.quantity) + '</b>';
        }
        output += ', &nbsp&nbsp' + item.Label_Scale + ': <b>' + item.scale + '</b>';
    }
    output += ', &nbsp&nbsp' + item.Label_Released + ': <b>' + addCommas(item.released) + '</b>';

    if (!forPrint)
        output += ', &nbsp&nbsp<a href=?top=all&asset=' + item.key + get_lang() + ' class="button ll-blue-bgc"><b>' + item.Label_Holders + '</b></a>';

    output += '<br>' + item.Label_AssetType + ': <b>' + item.assetTypeNameFull + '</b><br>';
    if (item.properties)
        output += item.Label_Properties + ': ' + item.properties + '<br>';

    output += item.Label_AssetType_Desc + ': ' + item.assetTypeDesc + '<br>';
    output += '</p>';

    output += itemFoot(item, forPrint);

    if (forPrint)
        return output;

    output += '<br>';

    output += '</td>';
    output += '</tr>';
    output += '</table>';

    output += '<h3>' + item.Label_Available_pairs + '</h3>';

    output += '<table border="0" cellspacing="10" class="tiny table table-striped" style="border: 1px solid #ddd;"><tr>';
    output += '<td><b>' + item.Label_Pair + '</b></td><td><b>' + item.Label_Orders_Count + '</b></td>';
    output += '<td><b>' + item.Label_Open_Orders_Volume + '</b></td>';
    output += '<td><b>' + item.Label_Trades_Count + '</b></td><td><b>' + item.Label_Trades_Volume + '</b></td></tr>';

    for (key in data.pairs) {
        var pair = data.pairs[key];
        output += '<tr>';

        output += '<td><b>';
        output += '<a href="?asset=' + key + get_lang() + '">';
        output += getAssetName2(key, data.pairs[key].assetName);

        output += '<td>' + pair.openOrdersCount;

        output += '<td nowrap>';
        output += '<a href="?asset=' + item.key + '&asset=' + key + get_lang() + '"><b>'
        output += addCommas(pair.last);
        output += '</b></a> / <a href="?asset=' + key + '&asset=' + item.key + get_lang() + '"><b>'
        output += addCommas(pair.lastReverse) + '</b></a><br>';

        output += addCommas(pair.ordersPriceVolume) + ' / ' + addCommas(pair.ordersAmountVolume);

        output += '<td>' + pair.tradesCount;

        output += '<td nowrap>';
        output += addCommas(pair.tradeAmountVolume) + ' ' + getAssetNameMini(key, pair.assetName);
        output += '<br>' + addCommas(pair.tradesPriceVolume) + ' ' + getAssetNameMini(item.key, item.name);

    }
    output += '<tr><td><b>' + data.Label_Total + ':';
    output += '<td>' + data.totalOpenOrdersCount;
    output += '<td>' + addCommas(data.totalOrdersVolume) + ' ' + getAssetNameMini(item.key, item.name);
    output += '<td>' + data.totalTradesCount;
    output += '<td>' + addCommas(data.totalTradesVolume) + ' ' + getAssetNameMini(item.key, item.name);
    output += '<td></td></tr></table>';


    return output;
}

function trades(data) {
    var output = "";

    output += lastBlock(data.lastBlock);

    output += '';

    output += '<h3 style="display:inline;"><a href="?asset=' + data.assetWant + '&asset=' + data.assetHave + get_lang()
        + '"><img src="img/exchange.png" style="width:1em"></a> '
        + data.Label_Trades + '</h3> ';

    output += '<a href="?asset=' + data.assetHave + '&asset=' + data.assetWant + get_lang() + '"><h3 style="display:inline;">';
    output += getAssetName2(data.assetHave, data.assetHaveName) + ' / ';
    output += getAssetName2(data.assetWant, data.assetWantName) + '</h3></a>';

    output += '<br>';

    output +='<div><div class="col-lg-5" style="padding-left:5em;">';
    output += '<h4>' + data.Label_Orders + '</h4>';

    output += '<table border="0" cellspacing="3" cellpadding="5" class="tiny table table-striped"'
        + 'style="width:100%; border: 1px solid #ddd; margin-bottom: 0px;">';

    var averageVolume = 1 * data.sellsSumAmountGood;
    if (1 * data.buysSumAmountGood > averageVolume)
        averageVolume = 1 * data.buysSumAmountGood;

    var width = data.sellsSumAmountGood;

    for (key in data.sells) {

        output += '<tr style="background-color: transparent">';

        var widthLocal = width;
        if (widthLocal > averageVolume) {
            widthLocal = averageVolume;
        }

        output += '<td style="position: relative">';
        output += '<span style="z-index: -1; width:' + 250 * widthLocal / averageVolume
                + '%; position:absolute; background-color:#ffe4e4; top: 0; bottom: 0; left: 0;"></span>';

        width -= data.sells[key].amount;

        output += '<span><a href ="?address=' + data.sells[key].creator_addr + get_lang() + '">' + cutBlank(data.sells[key].creator, 20) + '</a></span>';
        output += '<td><a href=?tx=' + key + get_lang() + '><b>' + addCommas(data.sells[key].price) + '</b</a>';
        output += '<td align=right><a href=?tx=' + key + get_lang() + '>' + addCommas(data.sells[key].amount) + '</a></tr>';

    }

    output += '<tr bgcolor="#f9f9f9">';
    output += '<td><td>' + data.Label_Total_For_Sell;
    ///output += '<td><b>' + addCommas(data.sellsSumTotalGood) + ' ' + getAssetNameMini(data.assetWant, data.assetWantName);
    output += ':<td><b>' + addCommas(data.sellsSumAmountGood) + ' ' + getAssetNameMini(data.assetHave, data.assetHaveName);

    output += '<tr bgcolor="#e0e0e0" style="background:#e0e0e0"><td width=40%><b>'
        + data.Label_Creator + ' / ' + data.Label_Amount + '<td width=30% style="font-size:1.4em"><b>' + data.Label_Price
        + '</b></td><td width=40%><b>' + data.Label_Amount + ' / ' + data.Label_Creator + '</b></td></tr>';

    output += '<tr bgcolor="#f9f9f9">';
    ///output += '<td><b>' + addCommas(data.buysSumTotalGood) + ' ' + getAssetNameMini(data.assetWant, data.assetWantName);
    output += '<td><b>' +  addCommas(data.buysSumAmountGood) + ' ' + getAssetNameMini(data.assetHave, data.assetHaveName);
    output += '<td>- ' + data.Label_Total_For_Buy + '<td>';

    width = 0;
    for (key in data.buys) {

        output += '<tr style="background-color: transparent">';
        output += '<td style="position: relative" align=right>';

        width += 1 * data.buys[key].buyingAmount; // преобразование строки в число

        var widthLocal = width;
        if (widthLocal > averageVolume) {
            widthLocal = averageVolume;
        }

        output += '<span style="position:absolute; z-index: -1; background-color:#cdfdcc; width:'
            + 250 * widthLocal / averageVolume + '%; top: 0; bottom: 0; left: 0;"></span>';

        output += '<span><a href=?tx=' + key + get_lang() + '>' + addCommas(data.buys[key].buyingAmount) + '</a></span>';
        output += '<td><a href=?tx=' + key + get_lang() + ' ><b>' + addCommas(data.buys[key].buyingPrice) + '</b></a>';
        output += '<td><a href ="?address=' + data.buys[key].creator_addr + get_lang() + '">' +
                cutBlank(data.buys[key].creator, 20) + '</a>';

    }

    output += '</table>';

    output += '</div><div class="col-lg-7" style="padding-right: 5em;">';

    output += '<h4 style="text-align: center;">' + data.Label_Trade_History + '</h4>';

    output += '<table border="0" cellspacing="3" cellpadding="5" class="tiny table table-striped" style="width:100%; vertical-align: baseline; border: 1px solid #ddd; fonf-size:0.8em">';
    output += '<tr bgcolor="#e0e0e0" style="background:#e0e0e0"><td align=center><b>' + data.Label_Date; // + '<td align=center><b>' + data.Label_Type + '</b></td>';
    output += '<td align=center><b>' + data.Label_Trade_Initiator
    output += '<td align=center><b>' + data.Label_Amount;
    output += '<td align=center><b>' + data.Label_Price;
    output += '<td align=center><b>' + data.Label_Total_Cost;
    output += '<td align=center><b>' + data.Label_Position_Holder
    output += '</tr>';

    for (key in data.trades) {

        var trade = data.trades[key];
        output += '<tr>';

        output += '<td align=center><a href=?trade=' + trade.initiatorTx + '/' + trade.targetTx + get_lang()
        output += '>' + convertTimestamp( trade.timestamp, false);

        output += '<td align=right style="line-height: 150%;">';

        if (trade.initiatorCreator_addr == data.assetWantOwner) {
            if (trade.type != 'sell') {
                output += '<span class="glyphicon glyphicon-arrow-up" style="color:limegreen"></span>';
            } else {
                output += '<span class="glyphicon glyphicon-arrow-down" style="color:crimson"></span>';
            }
        }

        output += '<a href=?address=' + trade.initiatorCreator_addr + '>' + cutBlank(trade.initiatorCreator, 20) + '</a>';

        if (trade.type == 'sell') {

            output += '<td align=right>' + addCommas(trade.amountHave);

            output += '<td align=left><span class="glyphicon glyphicon-arrow-down" style="color:crimson; font-size:1.2em"></span>'
                + '<b>' + addCommas(trade.realReversePrice) + '</b>';

            output += '<td align=right>' + addCommas(trade.amountWant);

        } else {

            output += '<td align=right>' + addCommas(trade.amountHave);

            output += '<td align=left><span class="glyphicon glyphicon-arrow-up" style="color:limegreen; font-size:1.2em"></span>'
                + '<b>' + addCommas(trade.realPrice) + '</b>';

            output += '<td align=right>' + addCommas(trade.amountWant);

        }

        output += '<td style="line-height: 150%;">';
        output += '<a href=?address=' + trade.targetCreator_addr + '>' + cutBlank(trade.targetCreator, 20) + '</a>';

        if (trade.targetCreator_addr == data.assetHaveOwner) {
            if (trade.type == 'sell') {
                output += '<span class="glyphicon glyphicon-arrow-up" style="color:limegreen"></span>';
            } else {
                output += '<span class="glyphicon glyphicon-arrow-down" style="color:crimson"></span>';
            }
        }

    }

    output += '</table>';

    output += '</div></div>';

    //output += '<b>' + data.Label_Trade_Volume + ':</b>&nbsp;&nbsp;&nbsp;&nbsp;' + addCommas(data.tradeHaveAmount) + ' ' + getAssetNameMini(data.assetHave, data.assetHaveName);
    //output += '&nbsp;&nbsp;&nbsp;&nbsp;' + addCommas(data.tradeWantAmount) + ' ' + getAssetNameMini(data.assetWant, data.assetWantName);

    output += '<br><br><b>' + data.Label_Go_To + ': <a href=?asset=' + data.assetHave + get_lang() + '>' + getAssetName2(data.assetHave, data.assetHaveName) + '</a>';
    output += '&nbsp;&nbsp;<a href=?asset=' + data.assetWant + get_lang() + '>' + getAssetName2(data.assetWant, data.assetWantName) + '</a>';
    output += '&nbsp;&nbsp;<a href=?asset=' + data.assetWant + '&asset=' + data.assetHave + get_lang() + '>' + getAssetName2(data.assetWant, data.assetWantName) + '/' + getAssetName2(data.assetHave, data.assetHaveName);
    output += '</b>';

    return output;
}