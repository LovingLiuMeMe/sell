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
                    <form role="form" method="post" action="/sell/seller/product/save">
                        <div class="form-group">
                            <label>名称</label>
                            <input name="productName" class="form-control" type="text" value="${(productInfoVO.productName)!''}"/>
                        </div>
                        <div class="form-group">
                            <label>价格</label>
                            <input name="productPrice" type="number" class="form-control" value="${(productInfoVO.productPrice)!''}"/>
                        </div>
                        <div class="form-group">
                            <label>库存</label>
                            <input name="productStock" type="number" class="form-control" value="${(productInfoVO.productStock)!''}"/>
                        </div>
                        <div class="form-group">
                            <label>描述</label>
                            <input name="productDescription" type="text" class="form-control" value="${(productInfoVO.productDescription)!''}">
                        </div>
                        <div class="form-group">
                            <label>图片</label>
                            <img height="100" width="100" src="${(productInfoVO.productIcon)!''}" alt="">
                            <input name="productIcon" type="text" class="form-control" value="${(productInfoVO.productIcon)!''}"/>
                        </div>
                        <div class="form-group">
                            <label>类目</label>
                            <select name="categoryType" class="form-control">
                                <#list categoryList as category>
                                    <option value="${(category.categoryType)}"
                                            <#if (productInfoVO.categoryType)?? && productInfoVO.categoryType==category.categoryType>
                                    selected
                                            </#if>>
                                        ${category.categoryName}
                                    </option>
                                </#list>

                            </select>
                        </div>
                        <input hidden type="text" name="productId" value="${(productInfoVO.productId)!''}">

                        <button type="submit" class="btn btn-default">提交</button>
                    </form>
                </div>
            </div>
        </div>
    </div>

</div>
</body>
</html>