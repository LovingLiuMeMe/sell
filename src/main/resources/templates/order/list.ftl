<html>
<#include "../common/header.ftl">

<body>
<div id="wrapper" class="toggled">

    <#--边栏sidebar-->
    <#include "../common/nav.ftl">

    <#--主要内容content-->
    <div id="page-content-wrapper">
        <div class="container-fluid">
            <div class="row clearfix">
                <div class="col-md-12 column">
                    <table class="table table-bordered table-condensed">
                        <thead>
                        <tr>
                            <th>订单id</th>
                            <th>姓名</th>
                            <th>手机号</th>
                            <th>地址</th>
                            <th>金额</th>
                            <th>订单状态</th>
                            <th>支付状态</th>
                            <th>创建时间</th>
                            <th colspan="2">操作</th>
                        </tr>
                        </thead>
                        <tbody>

                        <#list orderVOPage.content as orderVO>
                            <tr>
                                <td>${orderVO.orderId}</td>
                                <td>${orderVO.buyerName}</td>
                                <td>${orderVO.buyerPhone}</td>
                                <td>${orderVO.buyerAddress}</td>
                                <td>${orderVO.orderAmount}</td>
                                <td>${orderVO.orderStatusMessage}</td>
                                <td>${orderVO.payStatusMessage}</td>
                                <td>${orderVO.createTime}</td>
                                <td><a href="/sell/seller/order/detail?orderId=${orderVO.orderId}">详情</a></td>
                                <td>
                                    <#if orderVO.orderStatusMessage == "新订单">
                                        <a href="/sell/seller/order/cancel?orderId=${orderVO.orderId}">取消</a>
                                    </#if>
                                </td>
                            </tr>
                        </#list>
                        </tbody>
                    </table>
                </div>

                <#--分页-->
                <div class="col-md-12 column">
                    <ul class="pagination pull-right">
                        <#if currentPage lte 1>
                            <li class="disabled"><a href="#">上一页</a></li>
                        <#else>
                            <li><a href="/sell/seller/order/list?pageNum=${currentPage - 1}&pageSize=${size}">上一页</a></li>
                        </#if>

                        <#list 1..orderVOPage.getTotalPages() as index>
                            <#if currentPage == index>
                                <li class="disabled"><a href="#">${index}</a></li>
                            <#else>
                                <li><a href="/sell/seller/order/list?pageNum=${index}&pageSize=${size}">${index}</a></li>
                            </#if>
                        </#list>

                        <#if currentPage gte orderVOPage.getTotalPages()>
                            <li class="disabled"><a href="#">下一页</a></li>
                        <#else>
                            <li><a href="/sell/seller/order/list?pageNum=${currentPage + 1}&pageSize=${size}">下一页</a></li>
                        </#if>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>

<#--播放音乐-->

<script src="https://cdn.bootcss.com/jquery/1.12.4/jquery.min.js"></script>
<script src="https://cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<script>
    var websocket=null;
    if('WebSocket' in window){
        websocket=new WebSocket('ws://sqmax.natapp1.cc/sell/webSocket');
    }else{
        alert('该浏览器不支持websocket');
    }
    websocket.onopen=function (ev) {
        console.log('建立连接');
    }
    websocket.onclose=function (ev) {
        console.log('连接关闭');
    }
    websocket.onmessage=function (ev) {
        console.log('收到消息：'+ev.data);
        //弹窗提醒，播放消息
        $('#myModal').modal('show');
        document.getElementById('notice').play();
    }
    window.onbeforeunload=function (ev) {
        websocket.close();
    }
</script>
</body>
</html>