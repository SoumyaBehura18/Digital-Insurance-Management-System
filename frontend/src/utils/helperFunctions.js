import { CheckCircle, XCircle, MessageCircle } from "lucide-vue-next";

export const getStatusClasses = (status) => {
  const statusClasses = {
    OPEN: "bg-green-100 text-white-800",
    "in-progress": "bg-yellow-100 text-yellow-800",
    CLOSED: "bg-gray-200 text-white-800",
    RESOLVED: "bg-blue-100 text-white-800",
  };
  return statusClasses[status] || "bg-gray-100 text-gray-800";
};

export const getStatusClassIcon = (status) => {
  if (status === "OPEN") return MessageCircle;
  else if (status === "RESOLVED") return CheckCircle;
  else if (status === "CLOSED") return XCircle;
  else return XCircle;
};

export const formatDate = (date) => {
  return new Intl.DateTimeFormat("en-US", {
    year: "numeric",
    month: "long",
    day: "numeric",
  }).format(new Date(date));
};
