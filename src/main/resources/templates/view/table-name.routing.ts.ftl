import {Routes} from '@angular/router';
import {${table.upperCamelCaseName}Component} from './${table.lowerCaseSubName}.component';
import {${table.upperCamelCaseName}DetailComponent} from './${table.lowerCaseSubName}-detail.component';
import {${table.upperCamelCaseName}EditComponent} from './${table.lowerCaseSubName}-edit.component';
import {InletComponent} from '../../../@theme/components/inlet.component';

export const ${table.lowerCamelCaseName}Routes: Routes = [
    {
        path: '${table.lowerCaseSubName}',
        component: InletComponent,
        children: [
            {
                path: '',
                component: ${table.upperCamelCaseName}Component,
            }, {
                path: 'detail',
                component: ${table.upperCamelCaseName}DetailComponent
            }, {
                path: 'new',
                component: ${table.upperCamelCaseName}EditComponent
            }, {
                path: 'edit',
                component: ${table.upperCamelCaseName}EditComponent
            }
        ]
    },

];

export const routed${table.upperCamelCaseName}Components = [
    ${table.upperCamelCaseName}Component,
    ${table.upperCamelCaseName}DetailComponent,
    ${table.upperCamelCaseName}EditComponent,
];
