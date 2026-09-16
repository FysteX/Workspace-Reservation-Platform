import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { AdminLoginComponent } from './admin-login/admin-login.component';
import { RegisterComponent } from './register-component/register-component';
import { WorkspaceDetailComponent } from './workspace-detail-component/workspace-detail-component';
import { MemberComponent } from './member-component/member-component';
import { ManagerComponent } from './manager-component/manager-component';
import { AdminComponent } from './admin-component/admin-component';
import { ResetPasswordComponent } from './reset-password-component/reset-password-component';
import { ResetPasswordLinkComponent } from './reset-password-link-component/reset-password-link-component';

export const routes: Routes = [
    {path: "admin-login", component: AdminLoginComponent},
    {path: "login", component: LoginComponent},
    {path: "register", component: RegisterComponent},
    {path: "workspace-detail", component: WorkspaceDetailComponent},
    {path: "member", component: MemberComponent},
    {path: "manager", component: ManagerComponent},
    {path: "admin", component: AdminComponent},
    {path: "reset-password", component: ResetPasswordComponent},
    {path: "reset-password-link", component: ResetPasswordLinkComponent}
];