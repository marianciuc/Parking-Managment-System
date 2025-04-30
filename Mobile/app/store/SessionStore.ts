import {IAuthResponse, IPage, ISession, ISessionList} from "@/app/interfaces";
import {makeAutoObservable} from "mobx";
import * as AuthService from "@/app/services/AuthService";
import {AxiosError} from "axios";
import handleError from "@/app/utils/ErrorHandler";
import SessionService from "../services/SessionService";

export class SessionStore {
    page: number = 0;
    pageSize: number = 10;
    sessionPage: ISessionList[] = [];
    currentSession: ISession | null = null;
    totalPages: number = 0;

    constructor() {
        makeAutoObservable(this);
    }

    setSessionPage(sessionPage: IPage<ISessionList>) {
        this.sessionPage = sessionPage.content;
        this.totalPages = sessionPage.totalPages;
        this.page = sessionPage.number;
    }

    fetchSessions = async (userId: string, page: number) => {
        try {
            const request = await SessionService.fetchSessions(userId, page, this.pageSize);
            if (request.data) {
                const sessionResponse: IPage<ISessionList> = request.data;
                this.setSessionPage(sessionResponse);
                console.log("sessionResponse", sessionResponse);
            } else {
                throw new AxiosError("unexpected message");
                console.log("unexpected message");
            }
        } catch (e: unknown) {
            handleError(e, "error session");
        }
    }


}

