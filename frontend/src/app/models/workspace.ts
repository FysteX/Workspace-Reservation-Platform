import { Room } from "./room"

export class Workspace {
    idWorkspace: number = 0
    name: string = ""
    city: string = ""
    likes: number = 0
    activeStatus: boolean = false
    adress: string = ""
    firmName: string = ""
    manager: string = ""
    tables: number = 5
    price: number = 0
    elements: Room[] = []
}