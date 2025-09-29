<template>
    <div class="min-h-screen bg-background">
        <!-- Header -->
        <header class="bg-background border-b border-border">
            <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                <div class="flex items-center justify-between h-16">
                    <!-- Logo -->
                    <div class="flex items-center gap-2">
                        <Shield class="w-8 h-8 text-brand-backgroundTheme" />
                        <span class="text-xl font-bold">InsureCore</span>
                    </div>

                    <!-- Right side -->
                    <div class="flex items-center gap-4">
                        <!-- Dark Mode Toggle -->
                      

                        <!-- Auth Buttons -->
                        <div class="flex items-center gap-2">
                            <button class="btn-ghost px-4 py-2 rounded" @click="goToLogin">
                                Login
                            </button>
                            <button class="btn px-4 py-2 rounded-xl bg-brand-backgroundTheme text-white" @click="goToRegister">
                                Register
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </header>

        <!-- Hero Section -->
        <main>
            <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-20 text-center">
                <h1 class="text-4xl md:text-6xl font-bold mb-6">
                    Your Complete
                    <span class="text-brand-backgroundTheme block">Insurance Solution</span>
                </h1>
                <p class="text-xl text-muted-foreground max-w-3xl mx-auto mb-8">
                    Manage all your insurance policies, file claims, and get support in one comprehensive platform.
                    Protecting what matters most with modern, digital-first insurance management.
                </p>
                <div class="flex flex-col sm:flex-row gap-4 justify-center">
                    <button class="text-lg px-8 py-3 rounded-xl bg-brand-backgroundTheme text-white" @click="goToLogin">
                        Get Started Today
                    </button>
                    <button class="text-lg px-8 py-3 rounded-xl border border-black">
                        Learn More
                    </button>
                </div>
            </div>

            <!-- Features Section -->
            <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-20">
                <div class="text-center mb-16">
                    <h2 class="text-3xl md:text-4xl font-bold mb-4">
                        Everything You Need
                    </h2>
                    <p class="text-xl text-muted-foreground">
                        Comprehensive insurance management made simple
                    </p>
                </div>

                <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
                    <!-- Feature Cards -->
                    <CardComponent class="p-6 text-center">
                        <div
                            class="w-12 h-12 bg-brand-backgroundTheme/10 rounded-lg flex items-center justify-center mx-auto mb-4">
                            <Shield class="w-6 h-6 text-brand-backgroundTheme" />
                        </div>
                        <h3 class="text-xl font-semibold mb-2">Policy Management</h3>
                        <p class="text-muted-foreground">
                            View, manage, and purchase insurance policies all in one place. Track coverage, premiums,
                            and renewals effortlessly.
                        </p>
                    </CardComponent>

                    <CardComponent class="p-6 text-center">
                        <div
                            class="w-12 h-12 bg-brand-backgroundTheme/10 rounded-lg flex items-center justify-center mx-auto mb-4">
                            <FileText class="w-6 h-6 text-brand-backgroundTheme" />
                        </div>
                        <h3 class="text-xl font-semibold mb-2">Quick Claims</h3>
                        <p class="text-muted-foreground">
                            Submit and track insurance claims with ease. Get real-time updates on claim status and
                            approvals.
                        </p>
                    </CardComponent>

                    <CardComponent class="p-6 text-center">
                        <div
                            class="w-12 h-12 bg-brand-backgroundTheme/10 rounded-lg flex items-center justify-center mx-auto mb-4">
                            <MessageSquare class="w-6 h-6 text-brand-backgroundTheme" />
                        </div>
                        <h3 class="text-xl font-semibold mb-2">24/7 Support</h3>
                        <p class="text-muted-foreground">
                            Get instant help with our AI chatbot or raise support tickets for personalized assistance.
                        </p>
                    </CardComponent>

                    <CardComponent class="p-6 text-center">
                        <div
                            class="w-12 h-12 bg-brand-backgroundTheme/10 rounded-lg flex items-center justify-center mx-auto mb-4">
                            <TrendingUp class="w-6 h-6 text-brand-backgroundTheme" />
                        </div>
                        <h3 class="text-xl font-semibold mb-2">Analytics Dashboard</h3>
                        <p class="text-muted-foreground">
                            Comprehensive insights into your insurance portfolio with detailed charts and analytics.
                        </p>
                    </CardComponent>

                    <CardComponent class="p-6 text-center">
                        <div
                            class="w-12 h-12 bg-brand-backgroundTheme/10 rounded-lg flex items-center justify-center mx-auto mb-4">
                            <Eye class="w-6 h-6 text-brand-backgroundTheme" />
                        </div>
                        <h3 class="text-xl font-semibold mb-2">Full Transparency</h3>
                        <p class="text-muted-foreground">
                            Clear, upfront pricing and policy terms. No hidden fees or surprises in your insurance
                            journey.
                        </p>
                    </CardComponent>

                    <CardComponent class="p-6 text-center">
                        <div
                            class="w-12 h-12 bg-brand-backgroundTheme/10 rounded-lg flex items-center justify-center mx-auto mb-4">
                            <Settings class="w-6 h-6 text-brand-backgroundTheme" />
                        </div>
                        <h3 class="text-xl font-semibold mb-2">Easy Integration</h3>
                        <p class="text-muted-foreground">
                            Seamlessly connect with existing insurance providers and import your current policies.
                        </p>
                    </CardComponent>
                </div>
            </div>

            <!-- Insurance Types Section -->
            <div class="bg-muted/30 py-20 bg-gray-50">
                <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                    <div class="text-center mb-16">
                        <h2 class="text-3xl md:text-4xl font-bold mb-4">
                            Coverage for Every Need
                        </h2>
                        <p class="text-xl text-muted-foreground">
                            Comprehensive insurance solutions tailored to your life
                        </p>
                    </div>

                    <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
                        <!-- Life Insurance -->
                        <CardComponent class="p-8 shadow-sm text-center">
                            <div
                                class="w-16 h-16 bg-brand-backgroundTheme/10 rounded-lg flex items-center justify-center mb-6">
                                <Shield class="w-8 h-8 text-brand-backgroundTheme" />
                            </div>
                            <h3 class="text-2xl font-semibold mb-4">Life Insurance</h3>
                            <p class="text-muted-foreground mb-6">
                                Protect your family's financial future with comprehensive life insurance coverage,
                                including term life, whole life, and universal life policies.
                            </p>
                            <ul class="space-y-2 text-sm">
                                <li class="flex items-center gap-2">
                                    <CheckCircle class="w-4 h-4 text-brand-backgroundTheme" />
                                    Death benefit protection
                                </li>
                                <li class="flex items-center gap-2">
                                    <CheckCircle class="w-4 h-4 text-brand-backgroundTheme" />
                                    Critical illness coverage
                                </li>
                                <li class="flex items-center gap-2">
                                    <CheckCircle class="w-4 h-4 text-brand-backgroundTheme" />
                                    Tax advantages
                                </li>
                            </ul>
                        </CardComponent>

                        <!-- Health Insurance -->
                        <CardComponent class="p-8 shadow-sm text-center">
                            <div
                                class="w-16 h-16 bg-brand-backgroundTheme/10 rounded-lg flex items-center justify-center mb-6">
                                <Plus class="w-8 h-8 text-brand-backgroundTheme" />
                            </div>
                            <h3 class="text-2xl font-semibold mb-4">Health Insurance</h3>
                            <p class="text-muted-foreground mb-6">
                                Comprehensive health coverage for individuals and families, including preventive care,
                                emergency services, and specialist treatments.
                            </p>
                            <ul class="space-y-2 text-sm">
                                <li class="flex items-center gap-2">
                                    <CheckCircle class="w-4 h-4 text-brand-backgroundTheme" />
                                    Cashless treatment
                                </li>
                                <li class="flex items-center gap-2">
                                    <CheckCircle class="w-4 h-4 text-brand-backgroundTheme" />
                                    Pre/post hospitalization
                                </li>
                                <li class="flex items-center gap-2">
                                    <CheckCircle class="w-4 h-4 text-brand-backgroundTheme" />
                                    Wellness programs
                                </li>
                            </ul>
                        </CardComponent>

                        <!-- Vehicle Insurance -->
                        <CardComponent class="p-8 shadow-sm text-center">
                            <div
                                class="w-16 h-16 bg-brand-backgroundTheme/10 rounded-lg flex items-center justify-center mb-6">
                                <Shield class="w-8 h-8 text-brand-backgroundTheme" />
                            </div>
                            <h3 class="text-2xl font-semibold mb-4">Vehicle Insurance</h3>
                            <p class="text-muted-foreground mb-6">
                                Complete protection for your vehicles with comprehensive coverage,
                                liability protection, and roadside assistance.
                            </p>
                            <ul class="space-y-2 text-sm">
                                <li class="flex items-center gap-2">
                                    <CheckCircle class="w-4 h-4 text-brand-backgroundTheme" />
                                    Collision coverage
                                </li>
                                <li class="flex items-center gap-2">
                                    <CheckCircle class="w-4 h-4 text-brand-backgroundTheme" />
                                    Theft protection
                                </li>
                                <li class="flex items-center gap-2">
                                    <CheckCircle class="w-4 h-4 text-brand-backgroundTheme" />
                                    24/7 roadside assistance
                                </li>
                            </ul>
                        </CardComponent>
                    </div>
                </div>
            </div>

            <!-- CTA Section -->
            <div class="py-20 text-center ">
                <div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
                    <h2 class="text-3xl md:text-4xl font-bold mb-4">Ready to Get Started?</h2>
                    <p class="text-xl text-muted-foreground mb-8">
                        Join thousands of customers who trust InsureCore for their insurance needs.
                        Get a quote in minutes and start protecting what matters most.
                    </p>
                    <div class="flex flex-col sm:flex-row gap-4 justify-center">
                        <button class="text-lg px-8 py-3 rounded-xl bg-brand-backgroundTheme text-white"
                           @click="goToRegister">
                            Create Account
                        </button>
                        <button class="text-lg px-8 py-3 rounded-xl order border-black  ">
                            Contact Sales
                        </button>
                    </div>
                </div>
            </div>
        </main>

        <!-- Footer -->
        <footer class="bg-muted/30 border-t bg-gray-50">
            <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12 text-muted-foreground">
                <div class="grid grid-cols-1 md:grid-cols-4 gap-8">
                    <div>
                        <div class="flex items-center gap-2 mb-4">
                            <Shield class="w-6 h-6 text-brand-backgroundTheme" />
                            <span class="text-lg font-bold">InsureCore</span>
                        </div>
                        <p>Your complete insurance management solution. Protecting what matters most.</p>
                    </div>
                    <div>
                        <h4 class="font-semibold mb-4">Products</h4>
                        <ul class="space-y-2">
                            <li>Life Insurance</li>
                            <li>Health Insurance</li>
                            <li>Vehicle Insurance</li>
                            <li>Business Insurance</li>
                        </ul>
                    </div>
                    <div>
                        <h4 class="font-semibold mb-4">Support</h4>
                        <ul class="space-y-2">
                            <li>Help Center</li>
                            <li>Contact Us</li>
                            <li>Claims Support</li>
                            <li>Live Chat</li>
                        </ul>
                    </div>
                    <div>
                        <h4 class="font-semibold mb-4">Company</h4>
                        <ul class="space-y-2">
                            <li>About Us</li>
                            <li>Careers</li>
                            <li>Press</li>
                            <li>Legal</li>
                        </ul>
                    </div>
                </div>
                <div class="border-t mt-8 pt-8 text-center">
                    <p>&copy; 2024 InsureCore. All rights reserved.</p>
                </div>
            </div>
        </footer>
    </div>
</template>
<script setup>

import { ref } from "vue";
import CardComponent from "@/components/UI/CardComponent.vue";
import {
    Shield,
    FileText,
    MessageSquare,
    TrendingUp,
    Eye,
    Settings,
    CheckCircle,
    Plus,
    Sun,
    Moon,
} from "lucide-vue-next";
import { useRouter } from "vue-router";
const router = useRouter();

const goToLogin = () => {
  router.push("/login");
};

const goToRegister = () => {
  router.push("/register");
};

</script>


<style scoped>
/* Add custom Tailwind overrides here if needed */
</style>
