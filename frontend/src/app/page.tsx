"use client";

import React, { useState } from 'react';
import { Search, Brain, Network, BookOpen, Star, Clock, Filter, Plus } from 'lucide-react';

export default function Home() {
  const [searchQuery, setSearchQuery] = useState('');

  const mockPapers = [
    { id: 1, title: 'Attention Is All You Need', authors: 'Vaswani et al.', year: 2017, citations: 94000, type: 'AI' },
    { id: 2, title: 'BERT: Pre-training of Deep Bidirectional Transformers', authors: 'Devlin et al.', year: 2018, citations: 72000, type: 'NLP' },
    { id: 3, title: 'Generative Adversarial Nets', authors: 'Goodfellow et al.', year: 2014, citations: 52000, type: 'ML' }
  ];

  return (
    <div className="min-h-screen bg-[#09090b] text-gray-200 flex flex-col md:flex-row font-sans">
      
      {/* Sidebar */}
      <aside className="w-full md:w-64 border-b md:border-b-0 md:border-r border-[#27272a] bg-[#09090b] p-4 flex flex-col gap-6 sticky top-0 md:h-screen">
        <div className="flex items-center gap-3 px-2 text-blue-500 font-bold text-xl tracking-tight">
          <Brain className="w-7 h-7" />
          <span>PaperMate AI</span>
        </div>

        <nav className="flex flex-col gap-2 mt-4 flex-1">
          <NavItem icon={<Network />} label="Knowledge Graph" active />
          <NavItem icon={<BookOpen />} label="Library" />
          <NavItem icon={<Star />} label="Saved Papers" />
          <NavItem icon={<Clock />} label="History" />
        </nav>

        <div className="mt-auto glass-panel p-4 rounded-xl flex flex-col gap-3">
          <p className="text-xs text-gray-400 font-semibold uppercase tracking-wider">AI Assistant</p>
          <p className="text-sm text-gray-300">Generate a literature review from your saved papers.</p>
          <button className="bg-blue-600 hover:bg-blue-500 text-white text-sm font-medium py-2 px-4 rounded-lg micro-animation">
            Generate Now
          </button>
        </div>
      </aside>

      {/* Main Content */}
      <main className="flex-1 p-6 lg:p-10 flex flex-col gap-8 h-screen overflow-y-auto">
        
        {/* Header / Search */}
        <header className="flex flex-col md:flex-row gap-4 items-center justify-between">
          <div className="relative w-full max-w-2xl group">
            <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400 group-focus-within:text-blue-500 transition-colors" />
            <input 
              type="text" 
              placeholder="Search by topic, author, or DOI... (e.g. LLMs)"
              className="w-full bg-[#18181b] border border-[#27272a] focus:border-blue-500 focus:ring-1 focus:ring-blue-500 rounded-xl py-3 pl-10 pr-4 text-gray-200 placeholder-gray-500 outline-none transition-all shadow-sm"
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
            />
          </div>
          <button className="flex items-center gap-2 bg-[#27272a] hover:bg-[#3f3f46] border border-[#3f3f46] text-sm py-2.5 px-4 rounded-xl transition-colors">
            <Filter className="w-4 h-4" /> Filters
          </button>
        </header>

        {/* Dashboard Grid */}
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6 flex-1 min-h-0">
          
          {/* Main Visualizer Area */}
          <div className="lg:col-span-2 glass-panel rounded-2xl p-6 flex flex-col relative overflow-hidden group">
            <div className="flex justify-between items-center mb-4 z-10 relative">
              <h2 className="text-lg font-semibold text-white">Research Network</h2>
              <div className="flex gap-2">
                <span className="px-3 py-1 text-xs bg-blue-500/20 text-blue-400 rounded-full border border-blue-500/20">302 Nodes</span>
                <span className="px-3 py-1 text-xs bg-purple-500/20 text-purple-400 rounded-full border border-purple-500/20">840 Edges</span>
              </div>
            </div>
            
            <div className="flex-1 w-full h-full rounded-xl bg-[#121214] border border-[#27272a] flex items-center justify-center relative overflow-hidden">
              {/* Mock Graph Simulation */}
              <div className="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-96 h-96 bg-blue-500/10 rounded-full blur-[100px]"></div>
              <Network className="w-24 h-24 text-gray-700 opacity-50 absolute" />
              <div className="absolute right-4 bottom-4 glass-panel p-3 text-xs text-gray-400 flex flex-col gap-1">
                <div className="flex items-center gap-2"><div className="w-2 h-2 rounded-full bg-blue-500"></div> Core Paper</div>
                <div className="flex items-center gap-2"><div className="w-2 h-2 rounded-full bg-green-500"></div> Foundational</div>
                <div className="flex items-center gap-2"><div className="w-2 h-2 rounded-full bg-purple-500"></div> Recent State-of-the-art</div>
              </div>
              <p className="z-10 text-gray-400 mt-32">Interactive Graph Engine Loaded</p>
            </div>
          </div>

          {/* List Area */}
          <div className="flex flex-col gap-4">
            <h2 className="text-lg font-semibold text-white">Trending Papers</h2>
            <div className="flex flex-col gap-4 overflow-y-auto pr-2 pb-10">
              {mockPapers.map(paper => (
                <div key={paper.id} className="glass-panel p-4 rounded-xl micro-animation cursor-pointer group">
                  <div className="flex justify-between items-start mb-2">
                    <span className="text-xs font-medium px-2 py-1 rounded bg-[#27272a] text-gray-300 group-hover:bg-blue-500/20 group-hover:text-blue-400 transition-colors">
                      {paper.year}
                    </span>
                    <button className="text-gray-500 hover:text-white transition-colors">
                      <Plus className="w-4 h-4" />
                    </button>
                  </div>
                  <h3 className="font-semibold text-gray-100 leading-tight mb-1">{paper.title}</h3>
                  <p className="text-sm text-gray-400 mb-3">{paper.authors}</p>
                  <div className="flex items-center gap-4 text-xs text-gray-500">
                    <span className="flex items-center gap-1">
                      <BookOpen className="w-3 h-3" /> {paper.citations.toLocaleString()} citations
                    </span>
                  </div>
                </div>
              ))}
            </div>
          </div>

        </div>
      </main>
    </div>
  );
}

function NavItem({ icon, label, active = false }: { icon: React.ReactNode, label: string, active?: boolean }) {
  return (
    <a href="#" className={`flex items-center gap-3 px-3 py-2.5 rounded-xl transition-all ${active ? 'bg-blue-500/10 text-blue-500 font-medium' : 'text-gray-400 hover:bg-[#18181b] hover:text-gray-200'}`}>
      <span className="w-5 h-5">{icon}</span>
      <span className="text-sm">{label}</span>
    </a>
  );
}
