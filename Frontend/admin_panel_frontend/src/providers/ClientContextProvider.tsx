"use client";

import React, {createContext, ReactNode} from "react";
import {UserDomainStore, UserStore, OwnerStore, ParkingStore, ApiKeysStore, TagStore} from "@/store";

const userStore = new UserStore();
const userDomainStore = new UserDomainStore();
const ownerStore = new OwnerStore();
const parkingStore = new ParkingStore();
const apiKeysStore = new ApiKeysStore();
const tagStore = new TagStore();

export interface AppState {
    userStore: UserStore;
    userDomainStore: UserDomainStore,
    ownerStore: OwnerStore,
    parkingStore: ParkingStore,
    apiKeysStore: ApiKeysStore,
    tagStore: TagStore,
}

export const Context = createContext<AppState>({
    userStore,
    userDomainStore,
    ownerStore,
    parkingStore,
    apiKeysStore,
    tagStore,
});

export default function ClientContextProvider({children}: { children: ReactNode }) {
    return (
        <Context.Provider value={{userStore, userDomainStore, ownerStore, parkingStore, apiKeysStore, tagStore}}>
            {children}
        </Context.Provider>
    );
}