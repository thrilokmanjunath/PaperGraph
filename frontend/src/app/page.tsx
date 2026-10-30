"use client";

import React, { useState, useMemo } from 'react';
import { Search, Brain, Network, BookOpen, Star, Clock, ZoomIn, ZoomOut, RotateCcw, X, ExternalLink, Sparkles, CheckCircle2 } from 'lucide-react';

interface PaperNode {
  id: number;
  title: string;
  authors: string;
  year: number;
  citations: number;
  type: 'AI' | 'NLP' | 'CV' | 'ML';
  doi: string;
  abstract: string;
  category: 'core' | 'foundational' | 'sota';
  x: number;
  y: number;
}

interface CitationEdge {
  source: number;
  target: number;
}

const SEED_PAPERS: PaperNode[] = [
  {
    id: 1,
    title: 'Attention Is All You Need',
    authors: 'Vaswani et al.',
    year: 2017,
    citations: 94000,
    type: 'NLP',
    doi: '10.48550/arXiv.1706.03762',
    abstract: 'The dominant sequence transduction models are based on complex recurrent or convolutional neural networks. We propose the Transformer, based solely on attention mechanisms.',
    category: 'core',
    x: 400,
    y: 250
  },
  {
    id: 2,
    title: 'BERT: Pre-training of Deep Bidirectional Transformers',
    authors: 'Devlin et al.',
    year: 2018,
    citations: 72000,
    type: 'NLP',
    doi: '10.48550/arXiv.1810.04805',
    abstract: 'We introduce BERT, designed to pre-train deep bidirectional representations from unlabeled text by jointly conditioning on both left and right context.',
    category: 'sota',
    x: 250,
    y: 140
  },
  {
    id: 3,
    title: 'Deep Residual Learning for Image Recognition',
    authors: 'He et al.',
    year: 2015,
    citations: 165000,
    type: 'CV',
    doi: '10.1109/CVPR.2016.90',
    abstract: 'Deeper neural networks are more difficult to train. We present a residual learning framework to ease the training of networks that are substantially deeper.',
    category: 'foundational',
    x: 180,
    y: 360
  },
  {
    id: 4,
    title: 'Generative Adversarial Nets',
    authors: 'Goodfellow et al.',
    year: 2014,
    citations: 52000,
    type: 'ML',
    doi: '10.48550/arXiv.1406.2661',
    abstract: 'We propose a new framework for estimating generative models via an adversarial process, in which we simultaneously train two models: a generative and a discriminative model.',
    category: 'foundational',
    x: 580,
    y: 380
  },
  {
    id: 5,
    title: 'Language Models are Few-Shot Learners (GPT-3)',
    authors: 'Brown et al.',
    year: 2020,
    citations: 31000,
    type: 'AI',
    doi: '10.48550/arXiv.2005.14165',
    abstract: 'Recent work has demonstrated substantial gains on many NLP tasks by pre-training on a large corpus of text followed by fine-tuning. We scale language models to 175B parameters.',
    category: 'sota',
    x: 550,
    y: 130
  },
  {
    id: 6,
    title: 'Adam: A Method for Stochastic Optimization',
    authors: 'Kingma & Ba',
    year: 2014,
    citations: 145000,
    type: 'ML',
    doi: '10.48550/arXiv.1412.6980',
    abstract: 'We introduce Adam, an algorithm for first-order gradient-based optimization of stochastic objective functions, based on adaptive estimates of lower-order moments.',
    category: 'foundational',
    x: 380,
    y: 420
  }
];

const SEED_EDGES: CitationEdge[] = [
  { source: 2, target: 1 }, // BERT cites Transformer
  { source: 5, target: 1 }, // GPT-3 cites Transformer
  { source: 1, target: 6 }, // Transformer cites Adam
  { source: 2, target: 6 }, // BERT cites Adam
  { source: 3, target: 6 }, // ResNet cites Adam
  { source: 5, target: 2 }  // GPT-3 cites BERT
];

export default function Home() {
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedPaper, setSelectedPaper] = useState<PaperNode | null>(SEED_PAPERS[0]);
  const [activeFilter, setActiveFilter] = useState<string>('ALL');
  const [zoomLevel, setZoomLevel] = useState<number>(1);
  const [panOffset, setPanOffset] = useState<{ x: number; y: number }>({ x: 0, y: 0 });
  const [isGeneratingReview, setIsGeneratingReview] = useState<boolean>(false);
  const [reviewGenerated, setReviewGenerated] = useState<boolean>(false);

  // Filter papers based on search query and category tab
  const filteredPapers = useMemo(() => {
    const q = searchQuery.toLowerCase().trim();
    return SEED_PAPERS.filter(paper => {
      const matchesFilter = activeFilter === 'ALL' || paper.type === activeFilter;
      const matchesSearch = !q ||
        paper.title.toLowerCase().includes(q) ||
        paper.authors.toLowerCase().includes(q) ||
        paper.doi.toLowerCase().includes(q) ||
        paper.type.toLowerCase().includes(q) ||
        paper.year.toString().includes(q);
      return matchesFilter && matchesSearch;
    });
  }, [searchQuery, activeFilter]);

  // Connected papers for the currently selected paper
  const connectedPaperIds = useMemo(() => {
    if (!selectedPaper) return new Set<number>();
    const ids = new Set<number>();
    SEED_EDGES.forEach(edge => {
      if (edge.source === selectedPaper.id) ids.add(edge.target);
      if (edge.target === selectedPaper.id) ids.add(edge.source);
    });
    return ids;
  }, [selectedPaper]);

  const handleGenerateReview = () => {
    setIsGeneratingReview(true);
    setTimeout(() => {
      setIsGeneratingReview(false);
      setReviewGenerated(true);
    }, 600);
  };

  return (
    <div className="min-h-screen bg-[#09090b] text-gray-200 flex flex-col md:flex-row font-sans">
      
      {/* Sidebar */}
      <aside className="w-full md:w-64 border-b md:border-b-0 md:border-r border-[#27272a] bg-[#09090b] p-4 flex flex-col gap-6 sticky top-0 md:h-screen z-20">
        <div className="flex items-center gap-3 px-2 text-blue-500 font-bold text-xl tracking-tight">
          <Brain className="w-7 h-7 text-blue-400" />
          <span>PaperMate AI</span>
        </div>

        <nav className="flex flex-col gap-2 mt-4 flex-1">
          <NavItem icon={<Network className="w-4 h-4" />} label="Knowledge Graph" active />
          <NavItem icon={<BookOpen className="w-4 h-4" />} label="Citation Library" />
          <NavItem icon={<Star className="w-4 h-4" />} label="Saved Papers" />
          <NavItem icon={<Clock className="w-4 h-4" />} label="Audit Telemetry" />
        </nav>

        {/* AI Assistant Synthesis Panel */}
        <div className="mt-auto bg-[#18181b] border border-[#27272a] p-4 rounded-xl flex flex-col gap-3 shadow-lg">
          <div className="flex items-center gap-2 text-xs text-blue-400 font-semibold uppercase tracking-wider">
            <Sparkles className="w-4 h-4" />
            <span>AI Literature Reviewer</span>
          </div>
          <p className="text-xs text-gray-400">
            Synthesize cross-paper consensus and citation lineages from selected network nodes.
          </p>
          <button 
            onClick={handleGenerateReview}
            disabled={isGeneratingReview}
            className="bg-blue-600 hover:bg-blue-500 disabled:bg-blue-800 text-white text-xs font-semibold py-2 px-3 rounded-lg transition-all flex items-center justify-center gap-2 shadow"
          >
            {isGeneratingReview ? "Synthesizing..." : (reviewGenerated ? "Update Review" : "Generate Review")}
          </button>
        </div>
      </aside>

      {/* Main Content */}
      <main className="flex-1 p-6 lg:p-8 flex flex-col gap-6 h-screen overflow-y-auto">
        
        {/* Header / Search & Seed Data Badge */}
        <header className="flex flex-col lg:flex-row gap-4 items-start lg:items-center justify-between">
          <div className="relative w-full max-w-2xl group">
            <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400 group-focus-within:text-blue-500 transition-colors" />
            <input 
              type="text" 
              placeholder="Search by topic, author, DOI, or keyword... (e.g., Vaswani, Attention, BERT)"
              className="w-full bg-[#18181b] border border-[#27272a] focus:border-blue-500 focus:ring-1 focus:ring-blue-500 rounded-xl py-3 pl-10 pr-4 text-gray-200 placeholder-gray-500 outline-none transition-all shadow-sm"
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
            />
          </div>

          <div className="flex items-center gap-3 flex-wrap">
            <span className="px-3 py-1.5 text-xs bg-emerald-500/10 text-emerald-400 rounded-lg border border-emerald-500/20 font-medium flex items-center gap-1.5">
              <CheckCircle2 className="w-3.5 h-3.5" /> DEMO SEED DATA (Verified Network)
            </span>
            <div className="flex gap-1 bg-[#18181b] p-1 rounded-xl border border-[#27272a]">
              {['ALL', 'NLP', 'CV', 'ML', 'AI'].map(tab => (
                <button
                  key={tab}
                  onClick={() => setActiveFilter(tab)}
                  className={`px-3 py-1 text-xs rounded-lg font-medium transition-all ${
                    activeFilter === tab ? 'bg-blue-600 text-white shadow' : 'text-gray-400 hover:text-white'
                  }`}
                >
                  {tab}
                </button>
              ))}
            </div>
          </div>
        </header>

        {/* Synthesis Modal / Banner when generated */}
        {reviewGenerated && (
          <div className="bg-blue-950/30 border border-blue-500/30 p-4 rounded-xl flex flex-col gap-2 relative animate-in fade-in duration-300">
            <button 
              onClick={() => setReviewGenerated(false)} 
              className="absolute top-3 right-3 text-gray-400 hover:text-white"
            >
              <X className="w-4 h-4" />
            </button>
            <div className="flex items-center gap-2 text-sm font-semibold text-blue-400">
              <Sparkles className="w-4 h-4" />
              <span>Grounded Literature Synthesis: Attention & Representation Evolution</span>
            </div>
            <p className="text-xs text-gray-300 leading-relaxed">
              The foundational transition from recurrence to self-attention marked by <em>Vaswani et al. (2017)</em> catalyzed bidirectional pretraining in <em>Devlin et al. (2018)</em> and autoregressive few-shot scaling in <em>Brown et al. (2020)</em>. Both architectures rely on stochastic adaptive first-order optimization formalized by <em>Kingma & Ba (2014)</em>.
            </p>
          </div>
        )}

        {/* Dashboard Grid */}
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6 flex-1 min-h-0">
          
          {/* Main Visualizer Area: Interactive SVG Knowledge Graph */}
          <div className="lg:col-span-2 bg-[#121214] border border-[#27272a] rounded-2xl p-6 flex flex-col relative overflow-hidden group shadow-xl">
            <div className="flex justify-between items-center mb-4 z-10 relative">
              <div className="flex items-center gap-2">
                <Network className="w-5 h-5 text-blue-400" />
                <h2 className="text-base font-semibold text-white">Citation Knowledge Graph</h2>
              </div>
              <div className="flex items-center gap-2">
                <span className="px-2.5 py-1 text-xs bg-blue-500/20 text-blue-400 rounded-full border border-blue-500/20">
                  {SEED_PAPERS.length} Nodes
                </span>
                <span className="px-2.5 py-1 text-xs bg-purple-500/20 text-purple-400 rounded-full border border-purple-500/20">
                  {SEED_EDGES.length} Citations
                </span>
                {/* Zoom Controls */}
                <div className="flex gap-1 ml-2 bg-[#18181b] border border-[#27272a] rounded-lg p-1">
                  <button 
                    onClick={() => setZoomLevel(prev => Math.min(prev + 0.2, 2.0))} 
                    className="p-1 text-gray-400 hover:text-white rounded"
                    title="Zoom In"
                  >
                    <ZoomIn className="w-3.5 h-3.5" />
                  </button>
                  <button 
                    onClick={() => setZoomLevel(prev => Math.max(prev - 0.2, 0.6))} 
                    className="p-1 text-gray-400 hover:text-white rounded"
                    title="Zoom Out"
                  >
                    <ZoomOut className="w-3.5 h-3.5" />
                  </button>
                  <button 
                    onClick={() => { setZoomLevel(1); setPanOffset({ x: 0, y: 0 }); }} 
                    className="p-1 text-gray-400 hover:text-white rounded"
                    title="Reset View"
                  >
                    <RotateCcw className="w-3.5 h-3.5" />
                  </button>
                </div>
              </div>
            </div>
            
            {/* Interactive Graph Surface */}
            <div className="flex-1 w-full h-full rounded-xl bg-[#0c0c0e] border border-[#27272a] relative overflow-hidden flex items-center justify-center">
              <svg 
                className="w-full h-full cursor-grab active:cursor-grabbing select-none"
                viewBox="0 0 800 500"
                style={{ transform: `scale(${zoomLevel}) translate(${panOffset.x}px, ${panOffset.y}px)`, transformOrigin: 'center center', transition: 'transform 0.15s ease-out' }}
              >
                <defs>
                  <linearGradient id="edgeGrad" x1="0%" y1="0%" x2="100%" y2="100%">
                    <stop offset="0%" stopColor="#3b82f6" stopOpacity="0.6" />
                    <stop offset="100%" stopColor="#8b5cf6" stopOpacity="0.6" />
                  </linearGradient>
                  <marker id="arrow" viewBox="0 0 10 10" refX="28" refY="5" markerWidth="6" markerHeight="6" orient="auto-start-reverse">
                    <path d="M 0 1 L 10 5 L 0 9 z" fill="#60a5fa" />
                  </marker>
                </defs>

                {/* Edges */}
                {SEED_EDGES.map((edge, i) => {
                  const src = SEED_PAPERS.find(p => p.id === edge.source);
                  const tgt = SEED_PAPERS.find(p => p.id === edge.target);
                  if (!src || !tgt) return null;
                  const isHighlighted = selectedPaper && (selectedPaper.id === src.id || selectedPaper.id === tgt.id);
                  return (
                    <line 
                      key={i}
                      x1={src.x}
                      y1={src.y}
                      x2={tgt.x}
                      y2={tgt.y}
                      stroke={isHighlighted ? "#60a5fa" : "#3f3f46"}
                      strokeWidth={isHighlighted ? 2.5 : 1.2}
                      strokeDasharray={isHighlighted ? "none" : "3,3"}
                      markerEnd="url(#arrow)"
                    />
                  );
                })}

                {/* Nodes */}
                {SEED_PAPERS.map(paper => {
                  const isSelected = selectedPaper?.id === paper.id;
                  const isConnected = connectedPaperIds.has(paper.id);
                  
                  let nodeColor = "#3b82f6"; // default blue (core)
                  if (paper.category === 'foundational') nodeColor = "#10b981"; // green
                  if (paper.category === 'sota') nodeColor = "#8b5cf6"; // purple
                  if (isSelected) nodeColor = "#f59e0b"; // amber for selected

                  return (
                    <g 
                      key={paper.id}
                      transform={`translate(${paper.x}, ${paper.y})`}
                      className="cursor-pointer transition-all duration-200"
                      onClick={() => setSelectedPaper(paper)}
                    >
                      {/* Aura ring for selected/connected */}
                      {(isSelected || isConnected) && (
                        <circle 
                          r={isSelected ? 32 : 26} 
                          fill={isSelected ? "rgba(245, 158, 11, 0.2)" : "rgba(59, 130, 246, 0.15)"} 
                          className="animate-pulse"
                        />
                      )}
                      
                      {/* Main Node Body */}
                      <circle 
                        r={20} 
                        fill="#18181b" 
                        stroke={nodeColor} 
                        strokeWidth={isSelected ? 3.5 : 2}
                      />
                      
                      {/* Node Label Text */}
                      <text 
                        y={-28} 
                        textAnchor="middle" 
                        fill={isSelected ? "#fef08a" : "#e4e4e7"} 
                        fontSize="11" 
                        fontWeight="600"
                      >
                        {paper.title.length > 24 ? paper.title.substring(0, 22) + "..." : paper.title}
                      </text>
                      
                      <text 
                        y={36} 
                        textAnchor="middle" 
                        fill="#a1a1aa" 
                        fontSize="9"
                      >
                        {paper.year} • {paper.type}
                      </text>
                    </g>
                  );
                })}
              </svg>

              {/* Legend Overlay */}
              <div className="absolute right-4 bottom-4 bg-[#18181b]/90 border border-[#27272a] p-3 rounded-xl text-xs text-gray-400 flex flex-col gap-1.5 shadow-lg backdrop-blur-sm pointer-events-none">
                <div className="flex items-center gap-2"><div className="w-2.5 h-2.5 rounded-full bg-blue-500"></div> Core Network Node</div>
                <div className="flex items-center gap-2"><div className="w-2.5 h-2.5 rounded-full bg-emerald-500"></div> Foundational Baseline</div>
                <div className="flex items-center gap-2"><div className="w-2.5 h-2.5 rounded-full bg-purple-500"></div> SOTA Architecture</div>
                <div className="flex items-center gap-2"><div className="w-2.5 h-2.5 rounded-full bg-amber-500"></div> Selected Node</div>
              </div>
            </div>

            {/* Selected Paper Details Drawer */}
            {selectedPaper && (
              <div className="mt-4 p-4 rounded-xl bg-[#18181b] border border-[#27272a] flex flex-col gap-2">
                <div className="flex justify-between items-start">
                  <div>
                    <span className="text-xs font-semibold px-2 py-0.5 rounded bg-blue-500/20 text-blue-400 mr-2">
                      {selectedPaper.type}
                    </span>
                    <span className="text-xs text-gray-400">{selectedPaper.year} • {selectedPaper.citations.toLocaleString()} Citations</span>
                    <h3 className="text-sm font-bold text-white mt-1">{selectedPaper.title}</h3>
                    <p className="text-xs text-gray-400">{selectedPaper.authors}</p>
                  </div>
                  <a 
                    href={`https://doi.org/${selectedPaper.doi}`} 
                    target="_blank" 
                    rel="noopener noreferrer"
                    className="flex items-center gap-1 text-xs text-blue-400 hover:text-blue-300"
                  >
                    <span>DOI</span> <ExternalLink className="w-3 h-3" />
                  </a>
                </div>
                <p className="text-xs text-gray-300 line-clamp-2 mt-1">{selectedPaper.abstract}</p>
              </div>
            )}
          </div>

          {/* List Area: Searchable Paper Explorer */}
          <div className="flex flex-col gap-3">
            <div className="flex justify-between items-center">
              <h2 className="text-base font-semibold text-white">Curated Papers ({filteredPapers.length})</h2>
              {searchQuery && (
                <button 
                  onClick={() => setSearchQuery('')}
                  className="text-xs text-blue-400 hover:text-blue-300"
                >
                  Clear search
                </button>
              )}
            </div>

            <div className="flex flex-col gap-3 overflow-y-auto pr-1 pb-10">
              {filteredPapers.length === 0 ? (
                <div className="p-6 text-center text-gray-400 text-xs bg-[#18181b] rounded-xl border border-[#27272a]">
                  No papers matching &ldquo;{searchQuery}&rdquo;. Try searching for &ldquo;Transformer&rdquo;, &ldquo;Devlin&rdquo;, or &ldquo;ResNet&rdquo;.
                </div>
              ) : (
                filteredPapers.map(paper => {
                  const isSelected = selectedPaper?.id === paper.id;
                  return (
                    <div 
                      key={paper.id} 
                      onClick={() => setSelectedPaper(paper)}
                      className={`p-4 rounded-xl cursor-pointer transition-all border ${
                        isSelected 
                          ? 'bg-blue-600/10 border-blue-500/50 shadow-md' 
                          : 'bg-[#18181b] border-[#27272a] hover:border-[#3f3f46]'
                      }`}
                    >
                      <div className="flex justify-between items-start mb-1.5">
                        <span className="text-xs font-medium px-2 py-0.5 rounded bg-[#27272a] text-gray-300">
                          {paper.year} • {paper.type}
                        </span>
                        <span className="text-xs text-gray-500 font-mono">
                          {paper.citations.toLocaleString()} cites
                        </span>
                      </div>
                      <h3 className="font-semibold text-sm text-gray-100 leading-snug mb-1">
                        {paper.title}
                      </h3>
                      <p className="text-xs text-gray-400">{paper.authors}</p>
                    </div>
                  );
                })
              )}
            </div>
          </div>

        </div>
      </main>
    </div>
  );
}

function NavItem({ icon, label, active = false }: { icon: React.ReactNode, label: string, active?: boolean }) {
  return (
    <a href="#" className={`flex items-center gap-3 px-3 py-2 rounded-xl transition-all ${active ? 'bg-blue-500/10 text-blue-400 font-medium' : 'text-gray-400 hover:bg-[#18181b] hover:text-gray-200'}`}>
      <span className="w-4 h-4">{icon}</span>
      <span className="text-xs font-medium">{label}</span>
    </a>
  );
}
