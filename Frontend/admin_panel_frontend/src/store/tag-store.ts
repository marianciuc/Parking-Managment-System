import {ToastMessage, TagDomain} from "@/interfaces";
import {makeAutoObservable} from "mobx";
import {handleAxiosError} from "@/lib/exception-handler";
import TagService from "@/services/tag-service";


export default class TagStore {
    private currentTag: TagDomain | null = null;
    private tags: TagDomain[] = [];

    public get getCurrentTag() {
        return this.currentTag;
    }

    public get getTags() {
        return this.tags;
    }

    public constructor() {
        makeAutoObservable(this);
    }

    public async searchTags(filter: {
        name: string;
        tagId: string;
    }): Promise<ToastMessage | void> {
        try {
            const res = await TagService.searchTags(filter);
            const data: TagDomain[] = res.data;
            this.tags = data;
            return;
        } catch (err) {
            return handleAxiosError(err);
        }
    }


    public async createTag({name, description}: { name: string, description: string }): Promise<ToastMessage> {
        try {
            const res = await TagService.createTag({name, description});
            const data: TagDomain = res.data;
            this.tags.push(data);
            return {
                message: "Tag Created Successfully",
                messageType: "success",
                statusCode: 200
            } as ToastMessage;
        } catch (err) {
            return handleAxiosError(err);
        }
    }
}

