export type Role = 'ADMIN' | 'IT_SPECIALIST' | 'TEACHER' | 'MANAGER';

export type EquipmentStatus =
  | 'AVAILABLE' | 'ASSIGNED' | 'IN_REPAIR' | 'LOST' | 'WRITTEN_OFF';

export type EquipmentCondition = 'NEW' | 'GOOD' | 'NEEDS_REPAIR' | 'BROKEN';

export type AssignmentStatus = 'ACTIVE' | 'RETURNED' | 'CANCELLED';

export type RequestStatus = 'NEW' | 'IN_PROGRESS' | 'DONE' | 'REJECTED';

export type Priority = 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL';

export interface UserResponse {
  id: number;
  fullName: string;
  email: string;
  role: Role;
  position?: string;
  department?: string;
  enabled: boolean;
  createdAt?: string;
}

export interface AuthResponse {
  token: string;
  tokenType: string;
  user: UserResponse;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  fullName: string;
  email: string;
  password: string;
  role?: Role;
  position?: string;
  department?: string;
}

export interface Category {
  id: number;
  name: string;
  description?: string;
}

export interface Room {
  id: number;
  name: string;
  building?: string;
  floor?: number;
  type?: string;
  description?: string;
}

export interface Equipment {
  id: number;
  name: string;
  inventoryNumber: string;
  serialNumber?: string;
  categoryId?: number;
  categoryName?: string;
  status: EquipmentStatus;
  condition: EquipmentCondition;
  roomId?: number;
  roomName?: string;
  responsibleUserId?: number;
  responsibleUserName?: string;
  purchaseDate?: string;
  price?: number;
  description?: string;
  createdAt?: string;
  updatedAt?: string;
}

export interface EquipmentRequest {
  name: string;
  inventoryNumber: string;
  serialNumber?: string;
  categoryId?: number | null;
  status: EquipmentStatus;
  condition: EquipmentCondition;
  roomId?: number | null;
  responsibleUserId?: number | null;
  purchaseDate?: string | null;
  price?: number | null;
  description?: string;
}

export interface Assignment {
  id: number;
  equipmentId: number;
  equipmentName: string;
  assignedToUserId?: number;
  assignedToUserName?: string;
  assignedToRoomId?: number;
  assignedToRoomName?: string;
  assignedById?: number;
  assignedByName?: string;
  assignedDate: string;
  returnDate?: string;
  status: AssignmentStatus;
  comment?: string;
}

export interface RepairRequest {
  id: number;
  title: string;
  description?: string;
  equipmentId?: number;
  equipmentName?: string;
  createdById?: number;
  createdByName?: string;
  assignedToId?: number;
  assignedToName?: string;
  status: RequestStatus;
  priority: Priority;
  createdAt?: string;
  updatedAt?: string;
  closedAt?: string;
}

export interface RepairComment {
  id: number;
  repairRequestId: number;
  authorId?: number;
  authorName?: string;
  text: string;
  createdAt?: string;
}

export interface PageResponse<T> {
  content: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
  last: boolean;
}

export interface DashboardSummary {
  totalEquipment: number;
  available: number;
  assigned: number;
  inRepair: number;
  writtenOff: number;
  lost: number;
  totalRequests: number;
  newRequests: number;
  inProgressRequests: number;
  doneRequests: number;
  equipmentByCategory: Record<string, number>;
  equipmentByStatus: Record<string, number>;
  latestRequests: RepairRequest[];
  latestEquipment: Equipment[];
}
