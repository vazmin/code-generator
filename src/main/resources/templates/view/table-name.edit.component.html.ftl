<form name="editForm" role="form" (ngSubmit)="save()" #editForm="ngForm">
<nb-card>
  <nb-card-header>
    {{ ${table.lowerCamelCaseName} && ${table.lowerCamelCaseName}.${searchColumnName} != null ? ${table.lowerCamelCaseName}.${searchColumnName} : '' }}
    {{ ${table.lowerCamelCaseName} && ${table.lowerCamelCaseName}.id ? '编辑' : '新增'}}
  </nb-card-header>

  <nb-card-body>
    <div *ngIf="${table.lowerCamelCaseName}" class="form-group">

        <ngx-alert></ngx-alert>
        <ngx-alert-error></ngx-alert-error>
        <#list columnList as column>
            <#if column.actualColumnName != "id">
        <div class="row">
          <div class="col-md-2 form-label">
            <#if !column.nullable>
            <span class="text-danger">*&nbsp;</span>
            </#if>
            <label for="${column.lowerCamelCaseName}"
                   ngxTranslate="${table.lowerCamelCaseName}.${column.lowerCamelCaseName}">${column.clearRemark}</label>
          </div>
          <div class="col-md-4">
            <div class="form-group">
              <input type="text" class="form-control"
                     placeholder="请输入{{'${table.lowerCamelCaseName}.${column.lowerCamelCaseName}'| translate}}"
                     [(ngModel)]="${table.lowerCamelCaseName}.${column.lowerCamelCaseName}"
                     name="${column.lowerCamelCaseName}"
                     #${column.lowerCamelCaseName}Input="ngModel"
                     [class.form-control-danger]="${column.lowerCamelCaseName}Input.invalid && ${column.lowerCamelCaseName}Input.touched"
                 <#if column_index == 1>autofocus </#if>required="required"
                     id="${column.lowerCamelCaseName}"/>
              <div
                  *ngIf="${column.lowerCamelCaseName}Input.touched && ${column.lowerCamelCaseName}Input.invalid">
                <small class="form-text error"
                       *ngIf="${column.lowerCamelCaseName}Input.errors?.required"
                       ngxTranslate="entity.validation.required">
                    ${column.lowerCamelCaseName}不能为空
                </small>
              </div>
            </div>
          </div>
        </div>
            </#if>
        </#list>
    </div>
  </nb-card-body>
  <nb-card-footer>
    <button type="submit" class="btn btn-danger mr-2"
            [disabled]="editForm.form.invalid || submitting"
            ngxTranslate="entity.action.save">保存</button>
    <button class="btn btn-primary"
            ngxTranslate="entity.action.back"
            routerLink="${app.url.frontend}">返回</button>
  </nb-card-footer>
</nb-card>
</form>