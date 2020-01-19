<nb-card>
    <nb-card-header>
    ${table.clearRemark} 详细信息
    </nb-card-header>

    <nb-card-body class="p-0">
        <ul *ngIf="${table.lowerCamelCaseName}" class="detail-list">
        <#list columnList as column>
          <li>
            <div class="field" ngxTranslate="${table.lowerCamelCaseName}.${column.lowerCamelCaseName}">${column.clearRemark}</div>
            <div class="value">{{${table.lowerCamelCaseName}.${column.lowerCamelCaseName}}}</div>
          </li>
        </#list>
        </ul>
    </nb-card-body>
    <nb-card-footer>
        <button class="btn btn-primary" routerLink="${app.url.frontend}">返回</button>
    </nb-card-footer>
</nb-card>