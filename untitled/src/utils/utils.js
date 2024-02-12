export function formatDate(dateString) {
    const options = { year: 'numeric', month: 'long', day: 'numeric', hour: '2-digit', minute: '2-digit', second: '2-digit', hour12: true };
    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', options);
}

export function truncateDescription(description, maxLength = 30) {
    return description.length > maxLength ? `${description.substring(0, maxLength)}...` : description;
}
