import {Page, ToastMessage, UserDomain} from "@/interfaces";
import {makeAutoObservable} from "mobx";
import {handleAxiosError} from "@/lib/exception-handler";
import UsersService from "@/services/users-service";

export default class UserDomainStore {
    private users: UserDomain[] = [];
    private currentPage: number = 0;
    private totalPages: number = 1;
    private size: number = 10;

    public get totalPagesCount(): number {
        return this.totalPages;
    }

    public get getUsers(): UserDomain[] {
        return this.users;
    }

    public get currentPageNumber(): number {
        return this.currentPage;
    }

    public get sizePerPage(): number {
        return this.size;
    }

    public constructor() {
        makeAutoObservable(this);
    }

    private setUsers(page: Page<UserDomain>) {
        this.currentPage = page.number;
        this.totalPages = page.size;
        this.size = page.total;
        this.users = page.content;
    }

    public async searchOwners(filter: {
        email: string;
        page: number;
        size: number;
        recordStatus: string;
        userType: string;
    }): Promise<ToastMessage | void> {
        try {
            const res = await UsersService.searchUsers(filter);
            const data: Page<UserDomain> = res.data;
            this.setUsers(data);
            return;
        } catch (err) {
            return handleAxiosError(err);
        }
    }


}